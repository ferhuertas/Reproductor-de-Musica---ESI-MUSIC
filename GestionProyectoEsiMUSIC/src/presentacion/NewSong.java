package presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import persistencia.ControllerBD;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class NewSong extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfNombre;
	private JTextField tfAlbum;
	private JTextField tfAutor;
	private JTextField tfRutaCancion;
	JLabel lblImagenAlbum = new JLabel("");
	private ImageIcon imagen;
	private JFrame frame;
	String imagencargada = null;
	public static ControllerBD controller;

	/**
	 * Launch the application.
	 */

	public NewSong(ControllerBD controller) {
		setResizable(false);
		setModal(true);
		this.controller = controller;
		initComponents();
	}

	/**
	 * Create the dialog.
	 */
	public void initComponents() {
		setTitle("A\u00F1adir nueva cancion - ESI MUSIC");
		setBounds(100, 100, 492, 425);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel label = new JLabel("Nombre");
			label.setBounds(12, 77, 45, 16);
			contentPanel.add(label);
		}
		{
			tfNombre = new JTextField();
			tfNombre.setBounds(12, 98, 135, 22);
			tfNombre.setColumns(10);
			contentPanel.add(tfNombre);
		}
		{
			JLabel label = new JLabel("Album");
			label.setBounds(12, 188, 36, 16);
			contentPanel.add(label);
		}
		{
			tfAlbum = new JTextField();
			tfAlbum.setBounds(12, 207, 135, 22);
			tfAlbum.setColumns(10);
			contentPanel.add(tfAlbum);
		}
		{
			JLabel label = new JLabel("Autor");
			label.setBounds(12, 135, 31, 16);
			contentPanel.add(label);
		}
		{
			tfAutor = new JTextField();
			tfAutor.setBounds(12, 153, 135, 22);
			tfAutor.setColumns(10);
			contentPanel.add(tfAutor);
		}
		{
			JLabel label = new JLabel("Ruta cancion");
			label.setBounds(12, 23, 135, 16);
			contentPanel.add(label);
		}
		{
			tfRutaCancion = new JTextField();
			tfRutaCancion.setBounds(12, 42, 328, 22);
			tfRutaCancion.setColumns(10);
			contentPanel.add(tfRutaCancion);
		}
		{
			JButton button = new JButton("Cargar Cancion");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fcAbrir = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
					fcAbrir.setFileFilter(filter);
					int valorDevuelto = fcAbrir.showOpenDialog(frame);
					if (valorDevuelto == JFileChooser.APPROVE_OPTION) {
						File file = fcAbrir.getSelectedFile();
						tfRutaCancion.setText(escapePath(file.getAbsolutePath()));
					}
				}
			});
			button.setBounds(341, 41, 121, 23);
			contentPanel.add(button);
		}
		{
			JButton button = new JButton("A\u00F1adir nueva canci\u00F3n");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String sql = "INSERT into Canciones VALUES ('" + tfRutaCancion.getText() + "', '"
							+ tfNombre.getText() + "', '" + tfAutor.getText() + "', '" + tfAlbum.getText() + "', '"
							+ imagencargada + "') ";

					try {
						Boolean envio = controller.mandarSqlinsert(sql);
						InterfazReproductor.actualizarTabla();
						dispose();

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			button.setBounds(12, 312, 166, 53);
			contentPanel.add(button);
		}

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(188, 77, 274, 288);
		contentPanel.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 269, 0 };
		gbl_panel.rowHeights = new int[] { 66, 224, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		{
			JButton button = new JButton("Cargar foto cancion");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fcAbrir = new JFileChooser();
					FileFilter imageFilter = new FileNameExtensionFilter("Archivos de imagenes",
							ImageIO.getReaderFileSuffixes());
					fcAbrir.setFileFilter(imageFilter);
					int valorDevuelto = fcAbrir.showOpenDialog(frame);
					if (valorDevuelto == JFileChooser.APPROVE_OPTION) {
						File file = fcAbrir.getSelectedFile();
						imagencargada = escapePath(file.getAbsolutePath());

						imagen = new ImageIcon(new ImageIcon(imagencargada).getImage().getScaledInstance(255, 255,
								Image.SCALE_SMOOTH));
						lblImagenAlbum.setIcon(imagen);

					}
				}
			});
			GridBagConstraints gbc_button = new GridBagConstraints();
			gbc_button.fill = GridBagConstraints.BOTH;
			gbc_button.insets = new Insets(0, 0, 5, 0);
			gbc_button.gridx = 0;
			gbc_button.gridy = 0;
			panel.add(button, gbc_button);
		}
		{

			GridBagConstraints gbc_lblImagenAlbum = new GridBagConstraints();
			gbc_lblImagenAlbum.gridx = 0;
			gbc_lblImagenAlbum.gridy = 1;
			panel.add(lblImagenAlbum, gbc_lblImagenAlbum);
		}
	}

	public static String escapePath(String path) {
		return path.replace("\\", "\\\\");
	}
}
