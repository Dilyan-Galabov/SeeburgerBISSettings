package bis.debug.mode.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/*
 * UserInterface.java
 *
 * created at 2018-08-17 by d.galabov <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */

/**
 * This is the UserInterface and the functionality of BIS UI Debug mode Long description for UserInterface. It is used to replace, remove
 * and append in files host.xml, vm.properties, run.bat content by specified regular expressions. Searching in parent directory and in
 * subdirectories using JTextField and JFileChooser. Can be specified different options like editting concrete file by clicking on specific
 * checkbox and then using the run button. Restore default button it is used for restoring all content in the 3 files mentioned before
 * default settings. And cancel button which is used for exitting the application. JTextArea field shows up what happened while we run the
 * application, like : successfull replaced, removed and appended content or it can be occur some errors.
 *
 * @author d.galabov
 */
public class UserInterface extends JFrame implements ActionListener
{

    private static final long serialVersionUID = 1L;
    private static final String SOFTWARE_FOLDER = "software";
    private static final String BIS_FOLDER = "C:\\bis6";
    private static final String REGEX_COMMENT = ".*(<!--)(?:[\\soptionvalue=\"-agentlib:jdwp=transport=dt_socket,address=@remote.debug.port@,server=y,suspend=n])+(-->).*";
    private static final String REGEX_REPLACE = ".*(-Xms[\\d]+(?:G|mb)\\s-Xmx[\\d]+(?:G|mb)).*";
    private static final String REGEX_PLACE_COMMENT = "(\\s<option value=\"-agentlib:jdwp=transport=dt_socket,address=@remote.debug.port@,server=y,suspend=n\"/>)";
    private static final String VM_PROPERTIES = "\\vm.properties";
    private static final String HOST_XML = "\\deployer\\domain\\pieces\\host.xml";
    private static final String REGISTER_BAT = "\\register.bat";
    private static Config cfg = new Config();
    private String getFolder = cfg.getProperty("folder");

    private JButton runButton;
    private JButton restoreDefaultsButton;
    private JButton clearButton;
    private JButton choosePathButton;
    private JLabel dirLabel;
    private JLabel editOptionsLabel;
    private JTextField pathField;
    private JCheckBox hostXmlCheckBox;
    private JCheckBox vmOptionsCheckBox;
    private JCheckBox registerBatCheckBox;
    private JTextArea logTextArea;
    private JScrollPane scrollTextArea;

    public UserInterface()
    {

        setLayout(new GridBagLayout());
        setResizable(false);
        setSize(510, 545);
        setTitle("Seeburger BIS Debug Mode Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image frameIcon=null;
        try
        {
            frameIcon = ImageIO.read(getClass().getClassLoader().getResource("images/settings.png"));
            setIconImage(frameIcon);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dirLabel = new JLabel("Dir:");
        dirLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 5, 0);

        add(dirLabel, gbc);


        pathField = new JTextField();

        if(getFolder != null)
        {
            pathField.setText(getFolder);
        }
        else
        {
            pathField.setText(BIS_FOLDER);
        }

        pathField.setEnabled(false);
        pathField.setFont(new Font("Times New Roman", Font.BOLD, 15));
        pathField.setBackground(Color.BLACK);
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 20, 5, 5);
        gbc.weightx = 0.5;
        gbc.ipady = 13;
        gbc.gridx = 0;
        gbc.gridy = 1;

        add(pathField, gbc);

        ImageIcon folderIcon = imageIconGetResources("images/folder.png");
        choosePathButton = new JButton(folderIcon);
        choosePathButton.setFocusPainted(false);
        choosePathButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 60);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.ipady = 0;
        gbc.ipadx = 0;

        add(choosePathButton, gbc);

        editOptionsLabel = new JLabel("Edit options: ");
        editOptionsLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        gbc.insets = new Insets(20, 50, 5, 5);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;

        add(editOptionsLabel, gbc);

        gbc.insets = new Insets(7, 90, 5, 5);
        hostXmlCheckBox = new JCheckBox("host.xml", true);
        hostXmlCheckBox.setFont(new Font("Times New Roman", Font.BOLD, 16));

        gbc.gridwidth = 2;
        gbc.weightx = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 3;

        add(hostXmlCheckBox, gbc);

        vmOptionsCheckBox = new JCheckBox("vm.properties", true);
        vmOptionsCheckBox.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;

        add(vmOptionsCheckBox, gbc);

        registerBatCheckBox = new JCheckBox("register.bat", true);
        registerBatCheckBox.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;

        add(registerBatCheckBox, gbc);

        ImageIcon runIcon = imageIconGetResources("images/run.png");
        runButton = new JButton("Run", runIcon);
        runButton.setIconTextGap(10);
        runButton.setFocusPainted(false);
        runButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.insets = new Insets(20, 20, 5, 0);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy = 6;
        gbc.ipady = 5;
        gbc.ipadx = 0;

        add(runButton, gbc);

        ImageIcon restoreIcon = imageIconGetResources("images/restore.png");
        restoreDefaultsButton = new JButton("Restore Defaults", restoreIcon);
        restoreDefaultsButton.setIconTextGap(10);
        restoreDefaultsButton.setFocusPainted(false);
        restoreDefaultsButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 0);
        gbc.gridx = 1;
        gbc.weightx = 0.1;
        gbc.gridy = 6;
        gbc.ipady = 5;
        gbc.ipadx = 100;

        add(restoreDefaultsButton, gbc);

        ImageIcon clearIcon = imageIconGetResources("images/clear.png");
        clearButton = new JButton("Clear", clearIcon);
        clearButton.setIconTextGap(10);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 20);
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.ipady = 5;
        gbc.ipadx = 120;

        add(clearButton, gbc);

        logTextArea = new JTextArea();
        logTextArea.setEnabled(false);
        logTextArea.setFont(new Font("Times New Roman", Font.BOLD, 15));
        logTextArea.setBackground(Color.BLACK);
        scrollTextArea = new JScrollPane(logTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTextArea.setBounds(40, 290, 300, 140);

        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.ipady = 150;
        gbc.ipady = 150;

        add(scrollTextArea, gbc);

        choosePathButton.addActionListener(this);
        runButton.addActionListener(this);
        clearButton.addActionListener(this);
        restoreDefaultsButton.addActionListener(this);



    }

    private ImageIcon imageIconGetResources(String imageName)
    {

        InputStream in = getClass().getClassLoader().getResourceAsStream(imageName);
        ImageIcon folderIcon = null;
        try
        {
            if (in != null)
            {
                folderIcon = new ImageIcon(ImageIO.read(in));
            }
            else
            {
                pathField.setText("Image " + imageName + " doesn't found!");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    // NOOP
                }
            }

        }
        return folderIcon;

    }


    @Override
    public void actionPerformed(ActionEvent e)

    {
        try
        {

            if (((hostXmlCheckBox.isSelected() == false) && (vmOptionsCheckBox.isSelected() == false)) && registerBatCheckBox.isSelected() == false)
            {
                logTextArea.append("Check one of the edit options above!\n");
            }
            if (e.getSource() == choosePathButton)
            {
                JFileChooser fc = new JFileChooser();
                if(getFolder != null)
                {
                    File currentDirectory = new File(getFolder);
                    if(currentDirectory.exists())
                    {
                        fc.setCurrentDirectory(currentDirectory);
                    }
                }
                else
                {
                    fc.setCurrentDirectory(new File(BIS_FOLDER));
                }

                fc.setDialogTitle("Choose directory");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int showDialog = fc.showOpenDialog(new UserInterface());
                if (showDialog == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    pathField.setText(file.getAbsolutePath());
                    pathField.setBackground(Color.BLACK);
                    runButton.setEnabled(true);
                    restoreDefaultsButton.setEnabled(true);
                }

            }
            else if (e.getSource() == runButton)
            {
                String findDirectory = searchDirectory(new File(pathField.getText()), SOFTWARE_FOLDER);
                if (findDirectory != null)
                {

                    if (hostXmlCheckBox.isSelected())
                    {
                        check(modifyFile(findDirectory, HOST_XML, REGEX_COMMENT, StandardCharsets.UTF_8, false, false), (findDirectory + HOST_XML));
                    }

                    if (vmOptionsCheckBox.isSelected())
                    {
                        check(modifyFile(findDirectory, VM_PROPERTIES, REGEX_REPLACE, StandardCharsets.ISO_8859_1, true, false), (findDirectory + VM_PROPERTIES));

                    }
                    if (registerBatCheckBox.isSelected())
                    {
                        List<String> content = new ArrayList<>();
                        content.add("CALL %BISAS_BIN%\\run-bisas.bat");
                        check(appendInFile(findDirectory, REGISTER_BAT, StandardCharsets.UTF_8, content), (findDirectory + REGISTER_BAT));
                    }

                }
                else
                {
                    checkIfDirectoryExists();
                }

            }
            else if (e.getSource() == restoreDefaultsButton)
            {
                String findDirectory = searchDirectory(new File(pathField.getText()), SOFTWARE_FOLDER);

                if (findDirectory != null)
                {
                    if (hostXmlCheckBox.isSelected())
                    {
                        checkForEdit(modifyFile(findDirectory, HOST_XML, REGEX_PLACE_COMMENT, StandardCharsets.UTF_8, false, true), (findDirectory + HOST_XML));
                    }
                    if (vmOptionsCheckBox.isSelected())
                    {
                        checkForEdit(modifyFile(findDirectory, VM_PROPERTIES, REGEX_REPLACE, StandardCharsets.ISO_8859_1, true, true), (findDirectory + VM_PROPERTIES));
                    }
                    if (registerBatCheckBox.isSelected())
                    {
                        checkForEdit(restoreAppendedInfo(findDirectory, REGISTER_BAT, StandardCharsets.UTF_8), (findDirectory + REGISTER_BAT));
                    }
                }
                else
                {
                    checkIfDirectoryExists();
                }
            }

            else if (e.getSource() == clearButton)
            {
                restoreDefaults();
            }
        }
        catch (IOException ex)
        {
            if (ex instanceof NoSuchFileException)
            {
                logTextArea.append("Files are not found!\n");
            }
        }

    }


    /**
     * Check if a directory is does not exists(if it is null)
     */
    private void checkIfDirectoryExists()
    {
        pathField.setText("Could not find directory " + "'" + SOFTWARE_FOLDER + "'");
        pathField.setBackground(Color.RED);
        runButton.setEnabled(false);
        restoreDefaultsButton.setEnabled(false);
    }


    /**
     * This method is used for checking if a statement is true and to print a messsage for successfully or unssuccessfully modified files
     *
     * @param check - true if everything is successfully, false otherwise
     * @param fileName - file name to specify the path
     */
    private void check(boolean check, String fileName)
    {
        if (check)
        {
            logTextArea.append("Successfully modified: " + fileName + " !\n");
        }
        else
        {
            logTextArea.append("Unssuccessfully modified: " + fileName + " !\n");
        }

    }


    /**
     * Same as check but only messages for editing files
     *
     * @param check - true if everything successfully, false otherwise
     * @param fileName file name
     */
    private void checkForEdit(boolean check, String fileName)
    {
        if (check)
        {
            logTextArea.append("Successfully edit : " + fileName + " !");
            logTextArea.append("\n");
        }
        else
        {
            logTextArea.append("Unssuccessfully edit: " + fileName + " !");
            logTextArea.append("\n");
        }
    }


    /**
     * @param root - file name
     * @param directory - directory that will be searched. In this API it is used "software" as a constant.
     * @return absolute path if the current directory is found, null otherwise
     */
    private String searchDirectory(File root, String directory)
    {

        String[] directories = root.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return new File(dir, name).isDirectory();
            }
        });

        if (directories != null)
        {
            for (String f : directories)
            {
                if (f.equals(directory))
                {
                    return root.getAbsolutePath() + "\\" + f;
                }
            }
        }

        return null;

    }



    /**
     * Used for modifying files(replacing and removing in host.xml and vm.properties). Can be used and for restoring the default file lines
     * using the restoreDefaults parameter.
     *
     * @param directory - directory path
     * @param fileName - file name
     * @param regex - regular expression
     * @param cs - charset
     * @param replaceInFile - true if we want to replace in file, false otherwise
     * @param restoreDefaults - it is options that can restore the default settings in the specific files(host.xml, vm.properties);
     * @return true if the file is written successfully, false otherwise;
     * @throws IOException - if I/O exception occurs
     */
    private boolean modifyFile(String directory, String fileName, String regex, Charset cs, boolean replaceInFile, boolean restoreDefaults) throws IOException
    {
        String placeComment = "<!--<option value=\"-agentlib:jdwp=transport=dt_socket,address=@remote.debug.port@,server=y,suspend=n\"/>-->";
        Pattern pattern = Pattern.compile(regex);
        File filePath = new File(directory + fileName);
        List<String> lines = Files.readAllLines(filePath.toPath(), cs);

        if (lines != null)
        {
            String replacement = null;
            int lineNumber = 0;

            for (String line : lines)
            {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find())
                {
                    if (restoreDefaults)
                    {
                        if (replaceInFile)
                        {
                            replacement = line.replace(matcher.group(1), "-Xms2G -Xmx4G");
                            lines.set(lineNumber, replacement);
                            break;
                        }
                        else
                        {
                            replacement = line.replace(matcher.group(1), placeComment);
                            lines.set(lineNumber, replacement);
                            break;
                        }
                    }
                    else
                    {
                        if (replaceInFile)
                        {
                            replacement = line.replace(matcher.group(1), "-Xms1G -Xmx2G");
                            lines.set(lineNumber, replacement);
                            break;
                        }
                        else
                        {
                            String removeCommentStart = line.replace(matcher.group(1), " ");
                            replacement = removeCommentStart.replace(matcher.group(2), " ");
                            lines.set(lineNumber, replacement);
                            break;
                        }
                    }

                }
                lineNumber++;
            }
            Files.write(filePath.toPath(), lines, cs);
            return true;
        }

        return false;
    }


    /**
     * Append text at the end of file.
     *
     * @param directory - directory path
     * @param fileName - file Name
     * @param cs - charset
     * @param content - content to append
     * @throws IOException - if I/O exception occurs
     * @return true if content is correctly added at the end of the file; false otherwise;
     */
    private boolean appendInFile(String directory, String fileName, Charset cs, List<String> content) throws IOException
    {
        if (content != null)
        {
            File filePath = new File(directory + fileName);
            Files.write(filePath.toPath(), content, cs, StandardOpenOption.APPEND);
            return true;
        }
        return false;

    }


    /**
     * Remove appended text("CALL %BISAS_BIN%\\run-bisas.bat") in the end of the file.
     *
     * @param directory - directory path
     * @param fileName - file name
     * @param cs - charset
     * @return true if everything is written successfully, false otherwise
     * @throws IOException - if I/O exception occurs
     */
    private boolean restoreAppendedInfo(String directory, String fileName, Charset cs) throws IOException
    {
        File filePath = new File(directory + fileName);
        List<String> lines = Files.readAllLines(filePath.toPath(), cs);
        String runBat = "CALL %BISAS_BIN%\\run-bisas.bat";

        if (lines != null)
        {

            int lineNumber = 0;

            for (String line : lines)
            {
                if (line.equals(runBat))
                {
                    lines.remove(lineNumber);
                    break;
                }
                lineNumber++;
            }
            Files.write(filePath.toPath(), lines, cs);
            return true;
        }

        return false;
    }


    /**
     * Restore default settings
     */
    private void restoreDefaults()
    {
        hostXmlCheckBox.setSelected(true);
        vmOptionsCheckBox.setSelected(true);
        registerBatCheckBox.setSelected(true);
        logTextArea.setText("");

        if(getFolder != null)
        {
            pathField.setText(getFolder);
        }
        else
        {
            pathField.setText(BIS_FOLDER);
        }

        pathField.setBackground(Color.BLACK);
        runButton.setEnabled(true);
        restoreDefaultsButton.setEnabled(true);

    }

}
