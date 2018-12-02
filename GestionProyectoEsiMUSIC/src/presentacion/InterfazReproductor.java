package presentacion;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dominio.Reproductor2;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import persistencia.ControllerBD;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.awt.event.ActionEvent;

import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SwingConstants;

public class InterfazReproductor extends javax.swing.JFrame implements BasicPlayerListener {

	private static JTable tableCanciones;
	static DefaultTableModel modeloTablaCanciones;
	public static ControllerBD controller;
	public static ResultSet rs;
	public JTable tablecanciones;
	private ImageIcon imagen;
	public String rutaimagen = null;
	public JLabel labelImagenAlbum;
	public Reproductor2 esim = new Reproductor2(this);
	public String rutacancion = null;
	public boolean songPaused = false;
	public JToggleButton btnMute;
	public JSlider slideVolumen;
	private int currentVolumeValue;
	private String namesongplaying;
	private String autorsongplaying;
	private JLabel lblSongPlaying;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					controller = new ControllerBD();
					controller.CrearConexion();

				} catch (SQLException ex) {
					System.out.println(ex);
				}
				try {
					new InterfazReproductor().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Create the application.
	 */
	public InterfazReproductor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setResizable(false);
		setTitle("ESI MUSIC");
		setIconImage(Toolkit.getDefaultToolkit().getImage(InterfazReproductor.class.getResource("/logo.jpg")));
		setBounds(100, 100, 522, 823);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JButton btnAddSong = new JButton();
		btnAddSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewSong nueva = new NewSong(controller);
				nueva.setVisible(true);
			}
		});
		btnAddSong.setBounds(31, 5, 54, 43);
		btnAddSong.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/addbutt.png")).getImage()
				.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		getContentPane().add(btnAddSong);

		JButton btnDeleteSong = new JButton();
		btnDeleteSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!tableCanciones.getSelectionModel().isSelectionEmpty()) {

					deleteSong();

				}

			}
		});
		btnDeleteSong.setBounds(87, 5, 54, 43);
		btnDeleteSong.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/quitbutt.png"))
				.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		getContentPane().add(btnDeleteSong);

		JButton btnArriba = new JButton();
		btnArriba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectPrevSong();
			}
		});
		btnArriba.setBounds(373, 5, 54, 43);
		btnArriba.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/upbutt.png")).getImage()
				.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		getContentPane().add(btnArriba);

		JButton btnAbajo = new JButton();
		btnAbajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNextSong();
			}
		});
		btnAbajo.setBounds(429, 5, 54, 43);
		btnAbajo.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/downbutt.png")).getImage()
				.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		getContentPane().add(btnAbajo);

		JScrollPane scrollPanelCanciones = new JScrollPane();
		scrollPanelCanciones.setBounds(31, 61, 452, 346);
		getContentPane().add(scrollPanelCanciones);

		String[] nombreColumnas = { "Nombre", "Autor", "Album" };
		modeloTablaCanciones = new DefaultTableModel(null, nombreColumnas) {

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		tableCanciones = new JTable(modeloTablaCanciones);
		tableCanciones.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				clickASong();
			}
		});

		tableCanciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCanciones.setAutoCreateColumnsFromModel(false);
		tableCanciones.setAutoCreateRowSorter(true);
		scrollPanelCanciones.setViewportView(tableCanciones);

		JPanel panel_Reproductor = new JPanel();
		panel_Reproductor.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Reproductor",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Reproductor.setBounds(25, 424, 464, 358);
		getContentPane().add(panel_Reproductor);
		panel_Reproductor.setLayout(null);

		JPanel panel_Volumen = new JPanel();
		panel_Volumen.setBounds(6, 232, 452, 52);
		panel_Reproductor.add(panel_Volumen);
		panel_Volumen.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Volumen.setLayout(null);

		btnMute = new JToggleButton();
		btnMute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btnMute.isSelected()) {
					currentVolumeValue = slideVolumen.getValue();
					slideVolumen.setValue(0);
				} else {
					slideVolumen.setValue(currentVolumeValue);
				}
			}
		});
		btnMute.setBounds(12, 8, 45, 36);
		panel_Volumen.add(btnMute);
		btnMute.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mutebutt.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

		slideVolumen = new JSlider();
		slideVolumen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				esim.setVolume(slideVolumen.getValue(), slideVolumen.getMaximum());

				if (slideVolumen.getValue() == 0) {
					btnMute.setSelected(true);
				} else {
					btnMute.setSelected(false);
				}
			}
		});
		slideVolumen.setBounds(57, 13, 383, 26);
		panel_Volumen.add(slideVolumen);

		JButton btnPlay = new JButton();
		btnPlay.setBounds(232, 299, 67, 52);
		panel_Reproductor.add(btnPlay);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playASong();
			}
		});
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		btnPlay.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/playbutt.png")).getImage()
				.getScaledInstance(42, 42, Image.SCALE_SMOOTH)));

		JButton btnPause = new JButton();
		btnPause.setBounds(164, 299, 67, 52);
		panel_Reproductor.add(btnPause);
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (songPaused == false) {
					esim.pause();
					songPaused = true;
				} else {
					esim.resume();
					songPaused = false;
				}
			}
		});
		btnPause.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/pausebutt.png")).getImage()
				.getScaledInstance(42, 42, Image.SCALE_SMOOTH)));

		JButton btnAnterior = new JButton();
		btnAnterior.setBounds(96, 308, 67, 43);
		panel_Reproductor.add(btnAnterior);
		btnAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (selectPrevSong()) {
					playASong();
				}

			}
		});
		btnAnterior.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/prevbutt.png")).getImage()
				.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));

		JButton btnNext = new JButton();
		btnNext.setBounds(301, 308, 67, 43);
		panel_Reproductor.add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectNextSong()) {
					playASong();
				}
			}
		});
		btnNext.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/nextbutt.png")).getImage()
				.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));

		JPanel panel_Imagen = new JPanel();
		panel_Imagen.setBounds(140, 13, 188, 182);
		panel_Reproductor.add(panel_Imagen);
		panel_Imagen.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagLayout gbl_panel_Imagen = new GridBagLayout();
		gbl_panel_Imagen.columnWidths = new int[] { 269, 0 };
		gbl_panel_Imagen.rowHeights = new int[] { 224, 0 };
		gbl_panel_Imagen.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_Imagen.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_Imagen.setLayout(gbl_panel_Imagen);

		labelImagenAlbum = new JLabel("");
		GridBagConstraints gbc_labelImagenAlbum = new GridBagConstraints();
		gbc_labelImagenAlbum.gridx = 0;
		gbc_labelImagenAlbum.gridy = 0;
		panel_Imagen.add(labelImagenAlbum, gbc_labelImagenAlbum);

		lblSongPlaying = new JLabel("");
		lblSongPlaying.setHorizontalAlignment(SwingConstants.CENTER);
		lblSongPlaying.setBounds(96, 203, 272, 16);
		panel_Reproductor.add(lblSongPlaying);

		JSeparator separator = new JSeparator();
		separator.setBounds(31, 416, 452, 6);
		getContentPane().add(separator);
		try {
			actualizarTabla();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void actualizarTabla() throws SQLException {
		modeloTablaCanciones.setRowCount(0);

		String sql = "SELECT * FROM Canciones";
		rs = controller.mandarSql(sql);
		while (rs.next()) {
			modeloTablaCanciones.addRow(new Object[] { rs.getString(2), rs.getString(3), rs.getString(4) });

		}
		tableCanciones.setModel(modeloTablaCanciones);

	}

	private boolean selectPrevSong() {
		int nextIndex = tableCanciones.getSelectedRow() - 1;
		System.out.println(nextIndex);
		if (nextIndex >= 0) {
			tableCanciones.setRowSelectionInterval(nextIndex, nextIndex);
			return true;
		}

		return false;
	}

	private boolean selectNextSong() {
		int nextIndex = tableCanciones.getSelectedRow() + 1;
		if (nextIndex <= tableCanciones.getRowCount() - 1) {
			tableCanciones.setRowSelectionInterval(nextIndex, nextIndex);
			return true;
		}
		return false;
	}

	private void clickASong() {
		if (!tableCanciones.getSelectionModel().isSelectionEmpty()) {

			String sql = "SELECT rutacancion FROM Canciones WHERE nombre='"
					+ tableCanciones.getValueAt(tableCanciones.getSelectedRow(), 0) + "' AND autor='"
					+ tableCanciones.getValueAt(tableCanciones.getSelectedRow(), 1) + "'";
			try {
				rs = controller.mandarSql(sql);
				while (rs.next()) {
					rutacancion = rs.getString(1);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void playASong() {

		esim.play(rutacancion);
		esim.setVolume(slideVolumen.getValue(), slideVolumen.getMaximum());

		String sql = "SELECT nombre, autor, rutaimagen FROM Canciones WHERE rutacancion='"
				+ rutacancion.replace("\\", "\\\\") + "'";

		try {
			rs = controller.mandarSql(sql);
			while (rs.next()) {
				namesongplaying = rs.getString(1);
				autorsongplaying = rs.getString(2);
				rutaimagen = rs.getString(3);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lblSongPlaying.setText(namesongplaying + " - " + autorsongplaying);
		imagen = new ImageIcon(new ImageIcon(rutaimagen).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
		labelImagenAlbum.setIcon(imagen);
	}

	public void deleteSong() {
		String sql = "Delete FROM Canciones WHERE nombre='"
				+ tableCanciones.getValueAt(tableCanciones.getSelectedRow(), 0) + "' AND autor='"
				+ tableCanciones.getValueAt(tableCanciones.getSelectedRow(), 1) + "'";
		try {
			Boolean envio = controller.mandarSqlinsert(sql);
			actualizarTabla();

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	@Override
	public void opened(Object arg0, Map arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		// TODO Auto-generated method stub

	}
}
