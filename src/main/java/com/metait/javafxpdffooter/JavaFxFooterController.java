package com.metait.javafxpdffooter;

import com.metait.javafxpdffooter.passwrod.PasswordGenerator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.beans.value.ChangeListener;
import javafx.util.converter.IntegerStringConverter;
import javafx.beans.value.ObservableValue;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Pattern;

public class JavaFxFooterController {
    @FXML
    private TextField textFieldFooter;
    @FXML
    private Button buttonHandlePdf;
    @FXML
    private Button buttonOpenTmpGenPdf;
    @FXML
    private Button buttonSaveGenPdf;
    @FXML
    private Label labelMsg;
    @FXML
    private Button buttonOpenSavedPdf;
    @FXML
    private RadioButton radioButtonNoEncription;
    @FXML
    private RadioButton radioButtonReadEncription;
    @FXML
    private RadioButton radioButtonReadWriteEncription;
    @FXML
    private HBox hboxEncription;
    @FXML
    private RadioButton radioButtonRightPos;
    @FXML
    private RadioButton radioButtonLeftPos;
    @FXML
    private RadioButton radioButtonCenterPos;
    @FXML
    private TextField testFieldMinusPos;
    @FXML
    private TextField textFieldOwnerPassword;
    @FXML
    private TextField textFieldUserPassword;
    @FXML
    private TextField textFieldUserWritePassword;
    @FXML
    private TextField textFieldOwnerWritePassword;
    @FXML
    private HBox hboxUser;
    @FXML
    private RadioButton radioButtonWriteEncryption;
    @FXML
    private CheckBox checkBoxOnlyRightToRead;
    @FXML
    private CheckBox checkBoxALLOW_COPY;
    @FXML
    private CheckBox checkBoxALLOW_ASSEMBLY;
    @FXML
    private CheckBox checkBoxALLOW_DEGRADED_PRINTING;
    @FXML
    private CheckBox checkBoxALLOW_PRINTING;
    @FXML
    private CheckBox checkBoxALLOW_FILL_IN;
    @FXML
    private CheckBox checkBoxALLOW_MODIFY_ANNOTATIONS;
    @FXML
    private CheckBox checkBoxALLOW_MODIFY_CONTENTS;
    @FXML
    private CheckBox checkBoxALLOW_SCREENREADERS;
    @FXML
    private CheckBox checkBoxDO_NOT_ENCRYPT_METADATA;
    @FXML
    private RadioButton radioButtonNoFooter;
    @FXML
    private TextField textFieldFontSize;
    @FXML
    private CheckBox checkBoxStyle_NORMAL;
    @FXML
    private CheckBox checkBoxStyle_BOLD;
    @FXML
    private CheckBox checkBoxStyle_ITALIC;
    @FXML
    private CheckBox checkBoxStyle_UNDERLINE;
    @FXML
    private CheckBox checkBoxStyle_BOLDITALIC;
    @FXML
    private CheckBox checkBoxStyle_UNDEFINED;
    @FXML
    private ChoiceBox<String> choiseBoxFont;
    @FXML
    private CheckBox checkBoxStyle_STRIKETHRU;
    @FXML
    private Label labelInputFile;
    @FXML
    private TextArea textAreaResult;
    @FXML
    private Button buttonGenerateUserPwd;
    @FXML
    private Button buttonGenerateOwnerPwd;
    @FXML
    private TextField textFieldGenPwdLen;
    @FXML
    private VBox vBoxEncrypt;

    private int iFontSize = 14;
    private FileChooser filePdflChooser = new FileChooser();
    private FileChooser fileOutPdflChooser = new FileChooser();
    private Stage primaryStage;
    private final String [] arrFileExtenstions = new String[]
            {".pdf"};
    private File fPdfChooser = null;
    private File selectedFile = null;
    private File outputFile = null;
    private File tmpOutputFile = null;
    private boolean bWritenPdfFile = false;
    // private ChangeListener<Boolean> encryptListener = null;
    private ChangeListener<Boolean> radioPosListener = null;
  // private ChangeListener<Boolean> rightListener = null;
    private RadioButton earlierEncryptRadioButton = null;
    private boolean b_under_encryptionChanged = false;
    private boolean b_under_rightChanged = false;
    private final String cnstHELVETICA = "HELVETICA";
    private final String cnstCOURIER = "COURIER";
    private final String cnstTIMES_ROMAN = "TIMES_ROMAN";
    private final String cnstSYMBOL = "SYMBOL";
    private final String cnstZAPFDINGBATS = "ZAPFDINGBATS";
    private boolean bUnderStyleChanged = false;
    private int iGenPwdLen = 25;
    private final int iMaxGenPwdLen = 25;
    private boolean bGenPdwLen_under_change = false;
    private boolean passWordTextFieldChanged_under_change = false;

    private enum ENCCRIPTIONTYPE { NOEXCRYPTION, READENCRIPTION, WRITENCRYPTION, READWRITENENCRIPTION };
    private ENCCRIPTIONTYPE userSelectedEncription = ENCCRIPTIONTYPE.NOEXCRYPTION;

    public void setPrimaryStage(Stage stage)
    {
        primaryStage = stage;
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                boolean bOk = appIsClosing();
                Platform.exit();
                if (!bOk)
                    System.exit(1);
                else
                    System.exit(0);
            }
        });
    }

    public boolean appIsClosing()
    {
        System.out.println("appIsClosing");
        boolean ret = true;
        if (tmpOutputFile != null)
        {
            if (!tmpOutputFile.delete())
            {
               // ddddd
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning about a temporary file");
                alert.setHeaderText("Cannot remove a temporary pdf file!");
                String s ="Close file in an another application: " +tmpOutputFile.getAbsolutePath() +" before closing this warning!";
                alert.setContentText(s);
                // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
                alert.showAndWait();
                if (!tmpOutputFile.delete())
                    return false;
            }
        }
        return ret;
    }

    private void encryptRightCheckBoxes()
    {
        boolean setDisAbled = false;
        if (radioButtonNoEncription.isSelected()
           || radioButtonReadEncription.isSelected()) {
            setDisAbled = true;
            boolean setSelected = false;
            checkBoxOnlyRightToRead.setSelected(true);
            checkBoxALLOW_COPY.setSelected(setSelected);
            checkBoxALLOW_ASSEMBLY.setSelected(setSelected);
            checkBoxALLOW_DEGRADED_PRINTING.setSelected(setSelected);
            checkBoxALLOW_PRINTING.setSelected(setSelected);
            checkBoxALLOW_FILL_IN.setSelected(setSelected);
            checkBoxALLOW_MODIFY_ANNOTATIONS.setSelected(setSelected);
            checkBoxALLOW_MODIFY_CONTENTS.setSelected(setSelected);
            checkBoxALLOW_SCREENREADERS.setSelected(setSelected);
            checkBoxDO_NOT_ENCRYPT_METADATA.setSelected(setSelected);

            checkBoxALLOW_COPY.setDisable(true);
            checkBoxALLOW_ASSEMBLY.setDisable(true);
            checkBoxALLOW_DEGRADED_PRINTING.setDisable(true);
            checkBoxALLOW_PRINTING.setDisable(true);
            checkBoxALLOW_FILL_IN.setDisable(true);
            checkBoxALLOW_MODIFY_ANNOTATIONS.setDisable(true);
            checkBoxALLOW_MODIFY_CONTENTS.setDisable(true);
            checkBoxALLOW_SCREENREADERS.setDisable(true);
            checkBoxDO_NOT_ENCRYPT_METADATA.setDisable(true);
        }
        else
        {
            boolean setSelected = false;
            checkBoxOnlyRightToRead.setSelected(false);
            checkBoxALLOW_COPY.setSelected(setSelected);
            checkBoxALLOW_ASSEMBLY.setSelected(setSelected);
            checkBoxALLOW_DEGRADED_PRINTING.setSelected(setSelected);
            checkBoxALLOW_PRINTING.setSelected(setSelected);
            checkBoxALLOW_FILL_IN.setSelected(setSelected);
            checkBoxALLOW_MODIFY_ANNOTATIONS.setSelected(setSelected);
            checkBoxALLOW_MODIFY_CONTENTS.setSelected(setSelected);
            checkBoxALLOW_SCREENREADERS.setSelected(setSelected);
            checkBoxDO_NOT_ENCRYPT_METADATA.setSelected(setSelected);

            checkBoxALLOW_ASSEMBLY.setDisable(setSelected);
            checkBoxALLOW_DEGRADED_PRINTING.setDisable(setSelected);
            checkBoxALLOW_PRINTING.setDisable(setSelected);
            checkBoxALLOW_FILL_IN.setDisable(setSelected);
            checkBoxALLOW_MODIFY_ANNOTATIONS.setDisable(setSelected);
            checkBoxALLOW_MODIFY_CONTENTS.setDisable(setSelected);
            checkBoxALLOW_SCREENREADERS.setDisable(setSelected);
            checkBoxDO_NOT_ENCRYPT_METADATA.setDisable(setSelected);
        }
    }

    private void setDisableOrEnableRights(boolean setDisabled)
    {
        checkBoxALLOW_COPY.setDisable(setDisabled);
        checkBoxOnlyRightToRead.setDisable(setDisabled);
        checkBoxALLOW_ASSEMBLY.setDisable(setDisabled);
        checkBoxALLOW_DEGRADED_PRINTING.setDisable(setDisabled);
        checkBoxALLOW_PRINTING.setDisable(setDisabled);
        checkBoxALLOW_FILL_IN.setDisable(setDisabled);
        checkBoxALLOW_MODIFY_ANNOTATIONS.setDisable(setDisabled);
        checkBoxALLOW_MODIFY_CONTENTS.setDisable(setDisabled);
        checkBoxALLOW_SCREENREADERS.setDisable(setDisabled);
        checkBoxDO_NOT_ENCRYPT_METADATA.setDisable(setDisabled);
    }

    private void setSelectedOnOrOff(boolean setSelected, boolean bDoNotChangeOnlyRead)
    {
        if (!bDoNotChangeOnlyRead)
            checkBoxOnlyRightToRead.setSelected(setSelected);
        checkBoxALLOW_COPY.setSelected(setSelected);
        checkBoxALLOW_ASSEMBLY.setSelected(setSelected);
        checkBoxALLOW_DEGRADED_PRINTING.setSelected(setSelected);
        checkBoxALLOW_PRINTING.setSelected(setSelected);
        checkBoxALLOW_FILL_IN.setSelected(setSelected);
        checkBoxALLOW_MODIFY_ANNOTATIONS.setSelected(setSelected);
        checkBoxALLOW_MODIFY_CONTENTS.setSelected(setSelected);
        checkBoxALLOW_SCREENREADERS.setSelected(setSelected);
     //   checkBoxDO_NOT_ENCRYPT_METADATA.setSelected(setSelected);
    }

    private void setDisableRights()
    {
        setDisableOrEnableRights(true);
    }

    private void setEnableRights()
    {
        setDisableOrEnableRights(false);
    }

    private void disAbleOrEnablePasswordFields(boolean bValue)
    {
        testFieldMinusPos.setDisable(bValue);
        textFieldOwnerPassword.setDisable(bValue);
        textFieldUserPassword.setDisable(bValue);
        textFieldUserWritePassword.setDisable(bValue);
        textFieldOwnerWritePassword.setDisable(bValue);
        buttonGenerateOwnerPwd.setDisable(bValue);
        buttonGenerateUserPwd.setDisable(bValue);

        if (bValue) {
            textFieldOwnerPassword.setText("");
            textFieldUserPassword.setText("");
            textFieldUserWritePassword.setText("");
            textFieldOwnerWritePassword.setText("");
        }
    }

    private void encryptionChanged(RadioButton selectedRadioButton, boolean isNoEncrypt)
    {
        // if (b_under_encryptionChanged)
           // return;
        if (!selectedRadioButton.isSelected())
            return;
        b_under_encryptionChanged = true;
        if (earlierEncryptRadioButton == null)
        {
            if (radioButtonNoEncription.isSelected()
                || radioButtonReadEncription.isSelected())
            {
                setDisableRights();
                setSelectedOnOrOff(false, false);
                checkBoxOnlyRightToRead.setDisable(true);
                disAbleOrEnablePasswordFields(true);
                textFieldOwnerPassword.setDisable(false);
            }
            else
            {
                setEnableRights();
                setSelectedOnOrOff(false, false);
                checkBoxOnlyRightToRead.setSelected(true);
                if (selectedRadioButton.equals(radioButtonWriteEncryption)
                        && radioButtonWriteEncryption.isSelected())
                    Platform.runLater(new Runnable() {
                        public void run() {
                            boolean bValue = false;
                            testFieldMinusPos.setDisable(bValue);
                            textFieldOwnerPassword.setDisable(true);
                            textFieldUserWritePassword.setDisable(bValue);
                            textFieldOwnerWritePassword.setDisable(bValue);
                            buttonGenerateOwnerPwd.setDisable(bValue);
                            buttonGenerateUserPwd.setDisable(bValue);
                            textFieldUserPassword.setText("");
                            textFieldUserPassword.setDisable(true);
                        }
                    });
                else {
                    disAbleOrEnablePasswordFields(false);
                    if (radioButtonReadEncription.isSelected())
                        textFieldOwnerPassword.setDisable(false);
                }


                /*
                testFieldMinusPos.setDisable(true);
                textFieldOwnerPassword.setDisable(true);
                textFieldUserPassword.setDisable(true);
                buttonOpenSavedPdf.setDisable(true);
                buttonOpenTmpGenPdf.setDisable(true);
                buttonSaveGenPdf.setDisable(true);
                 */
            }
        }
        else
        {
            if (selectedRadioButton != null && selectedRadioButton.equals(earlierEncryptRadioButton))
                return;
            /*
            if (selectedRadioButton != null && (selectedRadioButton.equals(radioButtonNoEncription)
                || selectedRadioButton.equals(radioButtonReadEncription)
               && earlierEncryptRadioButton.equals(radioButtonNoEncription)
                  || earlierEncryptRadioButton.equals(radioButtonReadEncription)))
                return;
             */
            /*
            if (selectedRadioButton != null && (selectedRadioButton.equals(radioButtonReadWriteEncription)
                    || selectedRadioButton.equals(radioButtonWriteEncryption)
                    && earlierEncryptRadioButton.equals(radioButtonReadWriteEncription)
                    || earlierEncryptRadioButton.equals(radioButtonWriteEncryption)))
                return;
             */

            if (radioButtonNoEncription.isSelected()
                    || radioButtonReadEncription.isSelected())
            {
                setDisableRights();
                setSelectedOnOrOff(false, false);
                disAbleOrEnablePasswordFields(true);
                checkBoxOnlyRightToRead.setDisable(true);
                textFieldOwnerPassword.setDisable(false);
            }
            else
            {
                setEnableRights();
                setSelectedOnOrOff(false, false);
                checkBoxOnlyRightToRead.setSelected(true);
                if (selectedRadioButton.equals(radioButtonWriteEncryption)
                        && radioButtonWriteEncryption.isSelected())
                    Platform.runLater(new Runnable() {
                        public void run() {
                            boolean bValue = false;
                            testFieldMinusPos.setDisable(bValue);
                            textFieldOwnerPassword.setDisable(true);
                            textFieldUserWritePassword.setDisable(bValue);
                            textFieldOwnerWritePassword.setDisable(bValue);
                            buttonGenerateOwnerPwd.setDisable(bValue);
                            buttonGenerateUserPwd.setDisable(bValue);
                            textFieldUserPassword.setText("");
                            textFieldUserPassword.setDisable(true);
                        }
                    });
                else
                    disAbleOrEnablePasswordFields(false);
            }
        }

        /*
        encryptRightCheckBoxies();
        if (radioButtonNoEncription.isSelected())
        {
            textFieldUserPassword.setDisable(true);
            textFieldOwnerPassword.setDisable(true);
            textFieldUserWritePassword.setDisable(true);
            textFieldOnWritePassword.setDisable(true);
        }
        else
        {
            if (radioButtonReadWriteEncription.isSelected()) {
                textFieldUserPassword.setDisable(false);
                textFieldOwnerPassword.setDisable(false);
                textFieldUserWritePassword.setDisable(false);
                textFieldOnWritePassword.setDisable(false);
            }
            else
            {
                if (radioButtonWriteEncryption.isSelected())
                {
                    textFieldUserPassword.setDisable(true);
                    textFieldOwnerPassword.setDisable(true);
                    textFieldUserWritePassword.setDisable(false);
                    textFieldOnWritePassword.setDisable(false);
                }
                else
                {
                    textFieldUserPassword.setDisable(true);
                    textFieldOwnerPassword.setDisable(false);
                    textFieldUserWritePassword.setDisable(true);
                    textFieldOnWritePassword.setDisable(true);
                }
            }
        }
         */
        earlierEncryptRadioButton = selectedRadioButton;
        b_under_encryptionChanged = false;
    }

    private void styleChanged(CheckBox selectedCheckBox, boolean isNoRigthCheckBoxCalled)
    {
        if (!selectedCheckBox.isSelected())
            return;
        if (bUnderStyleChanged)
            return;
        bUnderStyleChanged = true;
        if (selectedCheckBox.equals(checkBoxStyle_NORMAL))
        {
            checkBoxStyle_BOLD.setSelected(false);
            checkBoxStyle_ITALIC.setSelected(false);
            checkBoxStyle_UNDERLINE.setSelected(false);
            checkBoxStyle_BOLDITALIC.setSelected(false);
            checkBoxStyle_STRIKETHRU.setSelected(false);
        }
        else
        {
            if (selectedCheckBox.equals(checkBoxStyle_BOLD)
                || selectedCheckBox.equals(checkBoxStyle_ITALIC)
                || selectedCheckBox.equals(checkBoxStyle_UNDERLINE)
                || selectedCheckBox.equals(checkBoxStyle_BOLDITALIC)
                || selectedCheckBox.equals(checkBoxStyle_STRIKETHRU))
             checkBoxStyle_NORMAL.setSelected(false);

            if (selectedCheckBox.equals(checkBoxStyle_BOLDITALIC))
            {
                checkBoxStyle_NORMAL.setSelected(false);
                checkBoxStyle_BOLD.setSelected(false);
                checkBoxStyle_ITALIC.setSelected(false);
            }
            else
            if (selectedCheckBox.equals(checkBoxStyle_BOLD) || selectedCheckBox.equals(checkBoxStyle_ITALIC))
            {
                checkBoxStyle_NORMAL.setSelected(false);
                checkBoxStyle_BOLDITALIC.setSelected(false);
            }
        }
        bUnderStyleChanged = false;
    }

    private void rightChanged(CheckBox selectedCheckBox, boolean isNoRigthCheckBoxCalled)
    {
        if (b_under_rightChanged)
            return;
        if (!selectedCheckBox.isSelected())
            return;
        b_under_rightChanged = true;
        if (selectedCheckBox != null && !selectedCheckBox.equals(checkBoxOnlyRightToRead))
            checkBoxOnlyRightToRead.setSelected(false);
        else
        if (selectedCheckBox != null)
            setSelectedOnOrOff(false, true);

        /*
        if (!isNoRigthCheckBoxCalled) {
            if ((checkBoxALLOW_ASSEMBLY.isSelected() ||
                    checkBoxALLOW_DEGRADED_PRINTING.isSelected() ||
                    checkBoxALLOW_PRINTING.isSelected() ||
                    checkBoxALLOW_FILL_IN.isSelected() ||
                    checkBoxALLOW_MODIFY_ANNOTATIONS.isSelected() ||
                    checkBoxALLOW_MODIFY_CONTENTS.isSelected() ||
                    checkBoxALLOW_SCREENREADERS.isSelected() ||
                    checkBoxDO_NOT_ENCRYPT_METADATA.isSelected())
                    && checkBoxOnlyRightToRead.isSelected()
            ) {
                checkBoxOnlyRightToRead.setSelected(false);
         */
                /*
            } else {
                checkBoxALLOW_ASSEMBLY.setSelected(false);
                checkBoxALLOW_DEGRADED_PRINTING.setSelected(false);
                checkBoxALLOW_PRINTING.setSelected(false);
                checkBoxALLOW_FILL_IN.setSelected(false);
                checkBoxALLOW_MODIFY_ANNOTATIONS.setSelected(false);
                checkBoxALLOW_MODIFY_CONTENTS.setSelected(false);
                checkBoxALLOW_SCREENREADERS.setSelected(false);
                checkBoxDO_NOT_ENCRYPT_METADATA.setSelected(false);
            }
                 */
        /*
            } else {
                if (checkBoxOnlyRightToRead.isSelected()) {
                    checkBoxALLOW_ASSEMBLY.setSelected(false);
                    checkBoxALLOW_DEGRADED_PRINTING.setSelected(false);
                    checkBoxALLOW_PRINTING.setSelected(false);
                    checkBoxALLOW_FILL_IN.setSelected(false);
                    checkBoxALLOW_MODIFY_ANNOTATIONS.setSelected(false);
                    checkBoxALLOW_MODIFY_CONTENTS.setSelected(false);
                    checkBoxALLOW_SCREENREADERS.setSelected(false);
                    checkBoxDO_NOT_ENCRYPT_METADATA.setSelected(false);

                    checkBoxALLOW_ASSEMBLY.setDisable(true);
                    checkBoxALLOW_DEGRADED_PRINTING.setDisable(true);
                    checkBoxALLOW_PRINTING.setDisable(true);
                    checkBoxALLOW_FILL_IN.setDisable(true);
                    checkBoxALLOW_MODIFY_ANNOTATIONS.setDisable(true);
                    checkBoxALLOW_MODIFY_CONTENTS.setDisable(true);
                    checkBoxALLOW_SCREENREADERS.setDisable(true);
                    checkBoxDO_NOT_ENCRYPT_METADATA.setDisable(true);
                } else {
                    checkBoxALLOW_ASSEMBLY.setDisable(false);
                    checkBoxALLOW_DEGRADED_PRINTING.setDisable(false);
                    checkBoxALLOW_PRINTING.setDisable(false);
                    checkBoxALLOW_FILL_IN.setDisable(false);
                    checkBoxALLOW_MODIFY_ANNOTATIONS.setDisable(false);
                    checkBoxALLOW_MODIFY_CONTENTS.setDisable(false);
                    checkBoxALLOW_SCREENREADERS.setDisable(false);
                    checkBoxDO_NOT_ENCRYPT_METADATA.setDisable(false);
                }
            }
        }
        else
        {
            checkBoxALLOW_ASSEMBLY.setSelected(false);
            checkBoxALLOW_DEGRADED_PRINTING.setSelected(false);
            checkBoxALLOW_PRINTING.setSelected(false);
            checkBoxALLOW_FILL_IN.setSelected(false);
            checkBoxALLOW_MODIFY_ANNOTATIONS.setSelected(false);
            checkBoxALLOW_MODIFY_CONTENTS.setSelected(false);
            checkBoxALLOW_SCREENREADERS.setSelected(false);
            checkBoxDO_NOT_ENCRYPT_METADATA.setSelected(false);

            checkBoxALLOW_ASSEMBLY.setDisable(true);
            checkBoxALLOW_DEGRADED_PRINTING.setDisable(true);
            checkBoxALLOW_PRINTING.setDisable(true);
            checkBoxALLOW_FILL_IN.setDisable(true);
            checkBoxALLOW_MODIFY_ANNOTATIONS.setDisable(true);
            checkBoxALLOW_MODIFY_CONTENTS.setDisable(true);
            checkBoxALLOW_SCREENREADERS.setDisable(true);
            checkBoxDO_NOT_ENCRYPT_METADATA.setDisable(true);
        }
         */
        b_under_rightChanged = false;
    }

    private void passWordTextFieldChanged(TextField tfChanged, String oldValue, String newValue)
    {
        if (tfChanged == null)
            return;
        if (newValue == null)
            return;
        if (passWordTextFieldChanged_under_change)
            return;
        passWordTextFieldChanged_under_change = true;
        if (newValue.trim().length() > iMaxGenPwdLen)
            tfChanged.setText(oldValue);
        passWordTextFieldChanged_under_change = false;
    }

    @FXML
    protected void initialize()
    {
        vBoxEncrypt.getChildren().remove(hboxUser); // not needed user password for a read input file! These comtrols
        // are in fxml file and in code also.

        Tooltip textFieldUserWritePasswordTip = new Tooltip(
                "The acrobat reader will ask to a user given password and will not allow a paste function!");
        textFieldUserWritePasswordTip.setStyle("-fx-font-weight: bold; -fx-text-fill: yellow; -fx-font-size: 14");
        textFieldUserWritePassword.setTooltip(textFieldUserWritePasswordTip);
        textFieldOwnerWritePassword.setTooltip(textFieldUserWritePasswordTip);

        TextFormatter<Integer> formatterGenPwdLen = new TextFormatter<>(
                new IntegerStringConverter(),
                iGenPwdLen,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );

        TextFormatter<Integer> formatterPos1 = new TextFormatter<>(
                new IntegerStringConverter(),
                45,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );

        TextFormatter<Integer> formatterPos2 = new TextFormatter<>(
                new IntegerStringConverter(),
                45,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );

        Tooltip textFieldGenPwdLenTip = new Tooltip(
                "A max length of password is 25");
        textFieldGenPwdLenTip.setStyle("-fx-font-weight: bold; -fx-text-fill: yellow; -fx-font-size: 14");

        textFieldGenPwdLen.setTooltip(textFieldGenPwdLenTip);;
        textFieldGenPwdLen.setText("" +iGenPwdLen);
        textFieldGenPwdLen.setTextFormatter(formatterGenPwdLen);
        textFieldGenPwdLen.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
            try {
                if (bGenPdwLen_under_change)
                    return;
                bGenPdwLen_under_change = true;
                int tmp_iGenPwdLen = Integer.parseInt(newValue);
                if (tmp_iGenPwdLen > iMaxGenPwdLen) {
                    textFieldGenPwdLen.setText(oldValue);
                }
                /*
                else
                if (tmp_iGenPwdLen < 10) {
                    textFieldGenPwdLen.setText(oldValue);
                }
                 */
                else
                    iGenPwdLen = tmp_iGenPwdLen;
                bGenPdwLen_under_change = false;
            }catch (Exception e){
                textFieldGenPwdLen.setText("");
            }
        }
    });

        textFieldFontSize.setTextFormatter(formatterPos1);
        textFieldFontSize.setText("" +iFontSize);
        textFieldFontSize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
                try {
                    iFontSize = Integer.parseInt(newValue);
                }catch (Exception e){
                }
            }
        });

        choiseBoxFont.getItems().add(cnstHELVETICA);
        choiseBoxFont.getItems().add(cnstCOURIER);
        choiseBoxFont.getItems().add(cnstTIMES_ROMAN);
        choiseBoxFont.getItems().add(cnstSYMBOL);
        choiseBoxFont.getItems().add(cnstZAPFDINGBATS);
        choiseBoxFont.getSelectionModel().select(0);

        hboxUser.setVisible(false);
        checkBoxOnlyRightToRead.setSelected(false);

        textFieldFooter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
                checkEnableButtons();
            }
        });

        /*
        rightListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(null, false);
            }
        };
         */

        /*
        this.encryptListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                encryptionChanged();
            }
        };
         */

        radioPosListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (radioButtonRightPos.isSelected() || radioButtonLeftPos.isSelected())
                    testFieldMinusPos.setDisable(false);
                else
                    testFieldMinusPos.setDisable(true);
                if (radioButtonNoFooter.isSelected()) {
                    textFieldFooter.setDisable(true);
                    checkBoxStyle_NORMAL.setDisable(true);
                    textFieldFontSize.setDisable(true);
                    choiseBoxFont.setDisable(true);
                    checkBoxStyle_BOLD.setDisable(true);
                    checkBoxStyle_ITALIC.setDisable(true);
                    checkBoxStyle_UNDERLINE.setDisable(true);
                    checkBoxStyle_BOLDITALIC.setDisable(true);
                    checkBoxStyle_STRIKETHRU.setDisable(true);
                    textFieldFooter.setText("");
                    checkEnableButtons();
                }
                else {
                    textFieldFooter.setDisable(false);
                    choiseBoxFont.setDisable(false);
                    checkBoxStyle_NORMAL.setDisable(false);
                    textFieldFontSize.setDisable(false);
                    checkBoxStyle_BOLD.setDisable(false);
                    checkBoxStyle_ITALIC.setDisable(false);
                    checkBoxStyle_UNDERLINE.setDisable(false);
                    checkBoxStyle_BOLDITALIC.setDisable(false);
                    checkBoxStyle_STRIKETHRU.setDisable(false);
                }
            }
        };

        radioButtonNoFooter.selectedProperty().addListener(radioPosListener);
        radioButtonRightPos.selectedProperty().addListener(radioPosListener);
        radioButtonLeftPos.selectedProperty().addListener(radioPosListener);
        radioButtonCenterPos.selectedProperty().addListener(radioPosListener);

        textFieldUserWritePassword.setDisable(true);
        textFieldOwnerWritePassword.setDisable(true);
        buttonGenerateOwnerPwd.setDisable(true);
        buttonGenerateUserPwd.setDisable(true);

        textFieldUserWritePassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
                passWordTextFieldChanged(textFieldUserWritePassword, oldValue, newValue);
            }
        });

        textFieldOwnerWritePassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
                passWordTextFieldChanged(textFieldOwnerWritePassword, oldValue, newValue);
            }
        });


        radioButtonNoEncription.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                encryptionChanged(radioButtonNoEncription, true);
            }
        });

        radioButtonReadEncription.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                encryptionChanged(radioButtonReadEncription, true);
            }
        });

        radioButtonReadWriteEncription.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                encryptionChanged(radioButtonReadWriteEncription, false);
            }
        });

        radioButtonWriteEncryption.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                encryptionChanged(radioButtonWriteEncryption, false);
            }
        });

        checkBoxALLOW_ASSEMBLY.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_ASSEMBLY, false);
            }
        });

        checkBoxOnlyRightToRead.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxOnlyRightToRead, true);
            }
        });

        checkBoxALLOW_DEGRADED_PRINTING.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_DEGRADED_PRINTING, false);
            }
        });

        checkBoxALLOW_PRINTING.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_PRINTING, false);
            }
        });

        checkBoxALLOW_FILL_IN.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_FILL_IN, false);
            }
        });

        checkBoxALLOW_MODIFY_ANNOTATIONS.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_MODIFY_ANNOTATIONS, false);
            }
        });

        checkBoxALLOW_MODIFY_CONTENTS.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_MODIFY_CONTENTS, false);
            }
        });

        checkBoxALLOW_SCREENREADERS.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxALLOW_SCREENREADERS, false);
            }
        });

        checkBoxDO_NOT_ENCRYPT_METADATA.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                rightChanged(checkBoxDO_NOT_ENCRYPT_METADATA, false);
            }
        });

        checkBoxStyle_NORMAL.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                styleChanged(checkBoxStyle_NORMAL, false);
            }
        });

        checkBoxStyle_BOLD.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                styleChanged(checkBoxStyle_BOLD, false);
            }
        });

        checkBoxStyle_ITALIC.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                styleChanged(checkBoxStyle_ITALIC, false);
            }
        });

        checkBoxStyle_UNDERLINE.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                styleChanged(checkBoxStyle_UNDERLINE, false);
            }
        });

        checkBoxStyle_BOLDITALIC.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                styleChanged(checkBoxStyle_BOLDITALIC, false);
            }
        });

        checkBoxStyle_STRIKETHRU.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                styleChanged(checkBoxStyle_STRIKETHRU, false);
            }
        });

        testFieldMinusPos.setTextFormatter(formatterPos2);

        testFieldMinusPos.setDisable(true);
        textFieldOwnerPassword.setDisable(true);
        textFieldUserPassword.setDisable(true);
        buttonOpenSavedPdf.setDisable(true);
        buttonOpenTmpGenPdf.setDisable(true);
        buttonSaveGenPdf.setDisable(true);

        filePdflChooser.setTitle("Open pdf file to handle");
        filePdflChooser.setInitialDirectory(new File("."));
        FileChooser.ExtensionFilter [] extFilters = getFileExtenstionFilters();
        for(FileChooser.ExtensionFilter ef : extFilters)
            filePdflChooser.getExtensionFilters().add(ef);

        fileOutPdflChooser.setTitle("Write pdf file with footer");
        fileOutPdflChooser.setInitialDirectory(new File("."));
        extFilters = getFileExtenstionFilters();
        for(FileChooser.ExtensionFilter ef : extFilters)
            fileOutPdflChooser.getExtensionFilters().add(ef);

       // this.hboxEncription.setSpacing(10);
      //  this.hboxEncription.setStyle("-fx-border-color: black");

        buttonHandlePdf.setFocusTraversable(true);
    }

    private FileChooser.ExtensionFilter [] getFileExtenstionFilters()
    {
        String strExts =  String.join(",", arrFileExtenstions);
        strExts = strExts.replaceAll("\\."," *.");
        strExts = "*.mp3";
        FileChooser.ExtensionFilter  [] ret = new FileChooser.ExtensionFilter[arrFileExtenstions.length];
        FileChooser.ExtensionFilter cur = null;
        int i = 0;
        for(String strExt : arrFileExtenstions)
        {
            cur = new FileChooser.ExtensionFilter("Audio files (*" +strExt +")", "*" +strExt);;
            ret[i++] = cur;
        }

        return ret;
    }

    private int getPdfRightsOfUserSelection()
    {
        int ret = 0;
        if (radioButtonNoEncription.isSelected())
            return -1;
        if (radioButtonReadEncription.isSelected())
            return -1;
        if (checkBoxOnlyRightToRead.isSelected())
            return 0;
        if (checkBoxALLOW_COPY.isSelected())
            ret = ret | PdfWriter.ALLOW_COPY;
        if (checkBoxALLOW_ASSEMBLY.isSelected())
            ret = ret | PdfWriter.ALLOW_ASSEMBLY;
        if (checkBoxALLOW_DEGRADED_PRINTING.isSelected())
            ret = ret | PdfWriter.ALLOW_DEGRADED_PRINTING;
        if (checkBoxALLOW_FILL_IN.isSelected())
            ret = ret | PdfWriter.ALLOW_FILL_IN;
        if (checkBoxALLOW_MODIFY_ANNOTATIONS.isSelected())
            ret = ret | PdfWriter.ALLOW_MODIFY_ANNOTATIONS;
        if (checkBoxALLOW_MODIFY_CONTENTS.isSelected())
            ret = ret | PdfWriter.ALLOW_MODIFY_CONTENTS;
        if (checkBoxALLOW_PRINTING.isSelected())
            ret = ret | PdfWriter.ALLOW_PRINTING;
        if (checkBoxALLOW_SCREENREADERS.isSelected())
            ret = ret | PdfWriter.ALLOW_SCREENREADERS;
     //   if (checkBoxDO_NOT_ENCRYPT_METADATA.isSelected())
       //   ret = ret | PdfWriter.DO_NOT_ENCRYPT_METADATA;

        return ret;
    }

    private int getPDFFont()
    {
        String strFount = choiseBoxFont.getSelectionModel().getSelectedItem();
        if (strFount == null || strFount.trim().length() == 0)
            return Font.HELVETICA;
        if (strFount != null && strFount.trim().equals(cnstHELVETICA))
            return Font.HELVETICA;
        if (strFount != null && strFount.trim().equals(cnstCOURIER))
            return Font.COURIER;
        if (strFount != null && strFount.trim().equals(cnstTIMES_ROMAN))
            return Font.TIMES_ROMAN;
        if (strFount != null && strFount.trim().equals(cnstSYMBOL))
            return Font.SYMBOL;
        if (strFount != null && strFount.trim().equals(cnstZAPFDINGBATS))
            return Font.ZAPFDINGBATS;
        return Font.TIMES_ROMAN;
    }

    private int getPDFFontStyle()
    {
        int ret = 0;
        if (checkBoxStyle_NORMAL.isSelected())
            ret = ret | Font.NORMAL;
        if (checkBoxStyle_BOLD.isSelected())
            ret = ret | Font.BOLD;
        if (checkBoxStyle_ITALIC.isSelected())
            ret = ret | Font.ITALIC;
        if (checkBoxStyle_UNDERLINE.isSelected())
            ret = ret | Font.UNDERLINE;
        if (checkBoxStyle_BOLDITALIC.isSelected())
            ret = ret | Font.BOLDITALIC;
        if (checkBoxStyle_STRIKETHRU.isSelected())
            ret = ret | Font.STRIKETHRU;

        return ret;
    }

    private String getEncryptOptionsString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("User rights: ");
        if (radioButtonNoEncription.isSelected() || radioButtonReadEncription.isSelected())
            sb.append("No write encryption.");
        else
        if (checkBoxOnlyRightToRead.isSelected()) {
            sb.append("User can only to read with password.");
            if (checkBoxDO_NOT_ENCRYPT_METADATA.isSelected())
                sb.append(" DO_NOT_ENCRYPT_METADATA");
        }
        else
        {
            if (checkBoxALLOW_COPY.isSelected())
                sb.append(" ALLOW_COPY");
            if (checkBoxALLOW_ASSEMBLY.isSelected())
                sb.append(" ALLOW_ASSEMBLY");
            if (checkBoxALLOW_DEGRADED_PRINTING.isSelected())
                sb.append(" ALLOW_DEGRADED_PRINTING");
            if (checkBoxALLOW_PRINTING.isSelected())
                sb.append(" ALLOW_PRINTING");
            if (checkBoxALLOW_FILL_IN.isSelected())
                sb.append(" ALLOW_FILL_IN");
            if (checkBoxALLOW_MODIFY_ANNOTATIONS.isSelected())
                sb.append(" ALLOW_MODIFY_ANNOTATIONS");
            if (checkBoxALLOW_MODIFY_CONTENTS.isSelected())
                sb.append(" ALLOW_MODIFY_CONTENTS");
            if (checkBoxALLOW_SCREENREADERS.isSelected())
                sb.append(" ALLOW_SCREENREADERS");

            if (checkBoxDO_NOT_ENCRYPT_METADATA.isSelected())
                sb.append(" DO_NOT_ENCRYPT_METADATA");
        }
        return sb.toString();
    }

    private void writeFooterIntopPdf(File fInput, File outputFile, String footertext,
                                     String userPassword, String ownerPassWord,
                                     String userWritePassword, String ownerWritePassWord,
                                     ENCCRIPTIONTYPE enccryption)
    throws IOException, DocumentException
    {
        if (fInput != null && fInput.exists() && (radioButtonNoFooter.isSelected() || footertext != null && footertext.trim().length()>0)) {
            try {
                PdfReader reader = null;
                try {
                    if (radioButtonNoEncription.isSelected() || radioButtonReadEncription.isSelected()
                            || radioButtonWriteEncryption.isSelected())
                        reader = new PdfReader(fInput.getAbsolutePath());
                    else
                        reader = new PdfReader(fInput.getAbsolutePath(), textFieldOwnerPassword.getText().getBytes());
                }catch (Exception e2){
                    labelMsg.setText(e2.getMessage());
                    e2.printStackTrace();
                }
                System.out.println("Document Metadata");
                System.out.println(reader.getInfo().toString());
                System.out.println("--");

                // footer
                /*
                final Rectangle footer = new Rectangle(30, 30, PageSize.A4.getRight(30), 180);
                footer.setBorder(Rectangle.BOX);
                footer.setBorderColor(Color.BLACK);
                footer.setBorderWidth(2);

                // creation of the document with a certain size and certain margins
                Document document = new Document(PageSize.A4, 40, 40, 200, 200);
                // document.setMargins(read)
                // content-box
                final Rectangle box = new Rectangle(footer);
                box.setBottom(document.bottom());
                 */
                try {
                    // creation of the different writers
                    //HtmlWriter.getInstance(document , System.out);
                    //  PdfWriter.getInstance(document , new FileOutputStream("text.pdf"));
                    // we add some meta information to the document
                    // document.addAuthor("Bruno Lowagie");
                    // document.addSubject("This is the result of a Test.");
                    // we open the document for writing
                    // document.open();

                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
                    int pdfWights = getPdfRightsOfUserSelection();
                  //  int iEncryptionMode = PdfWriter.ENCRYPTION_AES_128;

                    int iEncryptionMode = PdfWriter.STANDARD_ENCRYPTION_128;
                    if (checkBoxDO_NOT_ENCRYPT_METADATA.isSelected())
                        // this is a possible bug: this is not working: PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA;
                     //   iEncryptionMode =  PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA;
                        iEncryptionMode =  PdfWriter.STANDARD_ENCRYPTION_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA;

                    if (pdfWights != -1 && enccryption == ENCCRIPTIONTYPE.READWRITENENCRIPTION
                            || enccryption == ENCCRIPTIONTYPE.WRITENCRYPTION)
                    {
                        if (ownerWritePassWord != null && ownerWritePassWord.trim().length()>0
                                && userWritePassword != null && userWritePassword.trim().length()>0) {
                            // set password, user permissions and encryption
                            boolean strength = true;

                            if (enccryption == ENCCRIPTIONTYPE.WRITENCRYPTION)
                        //       stamper.setEncryption(userWritePassword.getBytes(), ownerWritePassWord.getBytes(),
                          //             pdfWights, iEncryptionMode);
                                stamper.setEncryption(iEncryptionMode, userWritePassword, ownerWritePassWord, pdfWights);
                            else
                                stamper.setEncryption(userWritePassword.getBytes(), ownerWritePassWord.getBytes(),
                                    /* PdfWriter.ALLOW_PRINTING */ pdfWights, iEncryptionMode);
                        }
                        else
                        if (pdfWights != -1 && ownerWritePassWord != null && ownerWritePassWord.trim().length()>0
                                && (userWritePassword == null || userWritePassword.trim().length()==0)) {
                            // set password, user permissions and encryption
                            boolean strength = true;
                            stamper.setEncryption(null, ownerWritePassWord.getBytes(),
                                    pdfWights, iEncryptionMode);
                        }
                        else
                        if ((pdfWights != -1 && ownerWritePassWord == null || ownerWritePassWord.trim().length()==0)
                                && userWritePassword != null && userWritePassword.trim().length()>0) {
                            // set password, user permissions and encryption
                            boolean strength = true;
                            stamper.setEncryption(userWritePassword.getBytes(), null,
                                    pdfWights, iEncryptionMode);
                        }
                    }

                    stamper.setRotateContents(false);
                    // Phrase t = new Phrase("Total pages " + reader.getNumberOfPages(), new Font(Font.HELVETICA, 14));
                    int iPdffont = getPDFFont();
                    int iPdffontStyle = getPDFFontStyle();
                    Phrase t = new Phrase(footertext, new Font(iPdffont, iFontSize, iPdffontStyle));

                   // if (radioButtonNoFooter.isSelected())
                     //   footertext = " ";

                    if (!radioButtonNoFooter.isSelected())
                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        float xt = 0;
                        if (radioButtonCenterPos.isSelected())
                            xt = reader.getPageSize(i).getWidth() - (reader.getPageSize(i).getWidth() / 2);
                        else
                        if (radioButtonLeftPos.isSelected()) {
                            xt = Integer.parseInt(testFieldMinusPos.getText());
                            if (xt < 25)
                                xt = 25;
                            else
                            if (xt > (reader.getPageSize(i).getWidth() -25))
                                xt = reader.getPageSize(i).getWidth() -25;
                        }
                        else
                        if (radioButtonRightPos.isSelected()) {
                            xt = reader.getPageSize(i).getWidth() - Integer.parseInt(testFieldMinusPos.getText());
                            if (xt < 25)
                                xt = 25;
                            else
                            if (xt > (reader.getPageSize(i).getWidth() - 25))
                                xt = reader.getPageSize(i).getWidth() -25;
                        }

                            float yt = reader.getPageSize(i).getBottom(15);
                        ColumnText.showTextAligned(
                                stamper.getOverContent(i), Element.ALIGN_CENTER,
                                t, xt, yt, 0);
                    }

                    stamper.setEnforcedModificationDate(Calendar.getInstance());

                    // Don't forget to add this line as no bytes are written to that output stream up until you close the PdfStamper instance.
                    stamper.close();
                 //   stamper.close();
                    reader.close();

                    // document.setFooter(footer);
                    /*
                    PdfObject pdfpage;

                    int numPages = reader.getNumberOfPages();
                    for (int index =1; index <= numPages; index++) {
                        byte[] pageBuf = reader.getPageContent(index);
                        pdfpage = reader.getPdfObject(index);
                        String pageContent = new String(pageBuf);
                        System.out.println("Page - " + index);
                        System.out.println(pageContent);
                        System.out.println("");
                        if (document.newPage()) {
                            int max = pdfpage.length();
                            for(int i = 0; i < max; i++)
                                document.add(pdfpage.getBytes(i));
                        }
                    }
                    reader.close();
                    */
                    // document.add(new Paragraph("Hello world"));

                    StringBuffer sbResult = new StringBuffer();
                    sbResult.append(outputFile.getAbsolutePath() +"\n"
                      +(radioButtonNoFooter.isSelected() ? "No footer,\n" : "Footer text: " +textFieldFooter.getText() +"\n")
                      +(radioButtonNoEncription.isSelected()
                            || radioButtonReadEncription.isSelected() ? "No encryption,\n" : getEncryptOptionsString() +"\n"));
                    textAreaResult.setText(sbResult.toString());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.err.println(ioe.getMessage());
                    labelMsg.setText(ioe.getMessage());
                    throw  ioe;
                } catch (DocumentException de) {
                    de.printStackTrace();
                    System.err.println(de.getMessage());
                    labelMsg.setText(de.getMessage());
                    throw de;
                }
//                if (reader != null)
                //                  reader.close();
                //  if (document != null)
                //    document.close();

                /*
                int numPages = reader.getNumberOfPages();
                for (int index = 1; index <= numPages; index++) {
                    byte[] pageBuf = reader.getPageContent(index);
                    String pageContent = new String(pageBuf);
                    System.out.println("Page - " + index);
                    System.out.println(pageContent);
                    System.out.println("");
                }
                reader.close();
                 */
            } catch (Exception e) {
                labelMsg.setText(e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }

    }

    private ENCCRIPTIONTYPE checkEnCryptionValues()
    {
        ENCCRIPTIONTYPE ret = ENCCRIPTIONTYPE.NOEXCRYPTION;
        if (!radioButtonNoEncription.isSelected())
        {
            if (radioButtonReadEncription.isSelected())
            {
                // if () dddddd
                if (textFieldOwnerPassword.getText().trim().length()==0)
                {
                    // TODO: aLERTA !!
                    return null;
                }
                ret = ENCCRIPTIONTYPE.READENCRIPTION;
            }
            else
            {
                if (radioButtonReadWriteEncription.isSelected())
                {
                    // if () dddddd
                    if (textFieldOwnerPassword.getText().trim().length()==0
                       && textFieldOwnerWritePassword.getText().trim().length()==0
                       && textFieldUserWritePassword.getText().trim().length()==0)
                    {
                        // TODO: aLERTA !!
                        return null;
                    }
                    ret = ENCCRIPTIONTYPE.READWRITENENCRIPTION;
                }
                else
                    if (radioButtonWriteEncryption.isSelected())
                    {
                        ret = ENCCRIPTIONTYPE.WRITENCRYPTION;
                    }
            }
        }
        return ret;
    }

    @FXML
    protected void pressedButtonSaveGenPdf() {
        // welcomeText.setText("Welcome to JavaFX Application!");
        // System.out.println("pressedButtonSaveGenPdf");
        // fileOutPdflChooser
        if (fPdfChooser != null)
            fileOutPdflChooser.setInitialDirectory(fPdfChooser);

        if (radioButtonNoFooter.isSelected() && (radioButtonNoEncription.isSelected()
                || radioButtonReadEncription.isSelected()))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about  footer and no encryption");
            alert.setHeaderText("The no footer and no or read encryption has been selected at the same time!");
            String s ="Select another combination of ui controls before pressing the gen output button.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }

        if ((radioButtonWriteEncryption.isSelected() || radioButtonReadWriteEncription.isSelected())
                && (textFieldOwnerWritePassword.getText() == null || textFieldOwnerWritePassword.getText().trim().length()==0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about empty owner password");
            alert.setHeaderText("The owner password text field is empty!");
            String s ="Full fill a correct (write) password text for a pdf output file.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }
        if ((radioButtonWriteEncryption.isSelected() || radioButtonReadWriteEncription.isSelected())
                && (textFieldUserWritePassword.getText() == null || textFieldUserWritePassword.getText().trim().length()==0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about empty user password");
            alert.setHeaderText("The user password text field is empty!");
            String s ="Full fill a correct user (write) password text for a pdf output file.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }

        if (!radioButtonNoFooter.isSelected() && (textFieldFooter.getText() == null || textFieldFooter.getText().trim().length()==0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about empty footer text");
            alert.setHeaderText("The footer text field is empty!");
            String s ="Full fill a correct footer text for a pdf output file.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }

        File outputFile2 = fileOutPdflChooser.showOpenDialog(this.primaryStage);
        if (selectedFile == null )
        {
            labelMsg.setText("Select at first input pdf file!");
            return;
        }
        if (outputFile2 == null)
        {
            labelMsg.setText("Select at first output pdf file!");
            return;
        }
        if (outputFile2 != null && selectedFile != null
                && outputFile2.getAbsolutePath().equals(selectedFile.getAbsolutePath()))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about same files");
            alert.setHeaderText("The input file and output file are the same!");
            String s ="File: " +selectedFile.getAbsolutePath() +"and output file are the same!\nThe save is stopped now!";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }
        outputFile = outputFile2;
        fPdfChooser = outputFile2.getParentFile();

        if (radioButtonNoFooter.isSelected() && (radioButtonNoEncription.isSelected()
            || radioButtonReadEncription.isSelected()))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about  footer and no encryption");
            alert.setHeaderText("The no footer and no or read encryption has been selected at the same time!");
            String s ="Select another combination of ui controls before pressing the gen output button.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }

        if (radioButtonNoFooter.isSelected()
                || (textFieldFooter.getText() != null && textFieldFooter.getText().trim().length()>0)) {
            userSelectedEncription = checkEnCryptionValues();
            if (userSelectedEncription ==  null)
                return;
            try {
                writeFooterIntopPdf(selectedFile, outputFile, textFieldFooter.getText(),
                        textFieldUserPassword.getText(),
                        textFieldOwnerPassword.getText(),
                        textFieldUserWritePassword.getText(),
                        textFieldOwnerWritePassword.getText(),
                        userSelectedEncription
                );
                bWritenPdfFile = true;
                labelMsg.setText("The output file has been write.");
                checkEnableButtons();
            }catch (Exception e){
                ;
            }
        }
    }

    private void checkEnableButtons()
    {
        if (selectedFile == null)
            return;
        if (radioButtonNoFooter.isSelected()
           || (selectedFile != null && textFieldFooter.getText().trim().length()>0 )) {
            buttonOpenTmpGenPdf.setDisable(false);
            buttonSaveGenPdf.setDisable(false);
        }
        if (bWritenPdfFile)
            buttonOpenSavedPdf.setDisable(false);
    }

    @FXML
    protected void pressedButtonHandlePdf() {
        // welcomeText.setText("Welcome to JavaFX Ap    plication!");
        System.out.println("pressedButtonHandlePdf");

        // filePdflChooser
        if (fPdfChooser != null)
            filePdflChooser.setInitialDirectory(fPdfChooser);

        selectedFile = filePdflChooser.showOpenDialog(this.primaryStage);
        if (selectedFile != null) {
            fPdfChooser = selectedFile.getParentFile();
            bWritenPdfFile = false;
            labelInputFile.setText(selectedFile.getAbsolutePath());

          //  textFieldUserPassword.setText("");
          //  textFieldOwnerPassword.setText("");
            textFieldUserWritePassword.setText("");
            textFieldOwnerWritePassword.setText("");
            checkEnableButtons();
        }
    }

    private File getTempPdfFile()
    {
        Path temp = null;
        try {
            temp = Files.createTempFile("tmp_", ".pdf");
            return temp.toFile();
        }catch (Exception e){
            labelMsg.setText(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    protected void pressedButtonOpenGenPdf() {
      //  System.out.println("pressedButtonOpenGenPdf");
        if (selectedFile == null )
        {
            labelMsg.setText("Select at first input pdf file!");
            return;
        }

        if (radioButtonNoFooter.isSelected() && (radioButtonNoEncription.isSelected()
            || radioButtonReadEncription.isSelected()))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about  footer and no encryption");
            alert.setHeaderText("The no footer and no or read encryption has been selected at the same time!");
            String s ="Select another combination of ui controls before pressing the gen output button.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }

        if (tmpOutputFile == null)
            tmpOutputFile = getTempPdfFile();
        if (tmpOutputFile != null && selectedFile != null
                && tmpOutputFile.getAbsolutePath().equals(selectedFile.getAbsolutePath()))
        {
            tmpOutputFile = getTempPdfFile();
            if (tmpOutputFile == null || selectedFile != null
                    && tmpOutputFile.getAbsolutePath().equals(selectedFile.getAbsolutePath())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning about same files");
                alert.setHeaderText("The input file and temporary output file are the same!");
                String s = "File: " + selectedFile.getAbsolutePath() + "and output file are the same! Try to press again!";
                alert.setContentText(s);
                // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
                alert.show();
                return;
            }
        }

        if (!radioButtonNoFooter.isSelected() && (textFieldFooter.getText() == null || textFieldFooter.getText().trim().length()==0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about empty footer text");
            alert.setHeaderText("The empty footer text field is empty!");
            String s ="Fullfill a coorect footer text for a pdf output file.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }

        if ((radioButtonWriteEncryption.isSelected() || radioButtonReadWriteEncription.isSelected())
                && (textFieldOwnerWritePassword.getText() == null || textFieldOwnerWritePassword.getText().trim().length()==0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about empty owner password");
            alert.setHeaderText("The owner password text field is empty!");
            String s ="Full fill a correct (write) password text for a pdf output file.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }
        if ((radioButtonWriteEncryption.isSelected() || radioButtonReadWriteEncription.isSelected())
                && (textFieldUserWritePassword.getText() == null || textFieldUserWritePassword.getText().trim().length()==0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning about empty user password");
            alert.setHeaderText("The user password text field is empty!");
            String s ="Full fill a correct user (write) password text for a pdf output file.";
            alert.setContentText(s);
            // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
            alert.show();
            return;
        }


        if (tmpOutputFile != null && selectedFile != null
           && textFieldFooter.getText() != null && textFieldFooter.getText().trim().length()>0) {
            userSelectedEncription = checkEnCryptionValues();
            if (userSelectedEncription ==  null)
                return;

            try {
                writeFooterIntopPdf(selectedFile, tmpOutputFile, textFieldFooter.getText(),
                        textFieldUserPassword.getText(), textFieldOwnerPassword.getText(),
                        textFieldUserWritePassword.getText(),
                        textFieldOwnerWritePassword.getText(),
                        userSelectedEncription
                );
                labelMsg.setText("The temporary output file has been write and to be open at pdf viewer.");
                try {
                    Desktop.getDesktop().browse(tmpOutputFile.toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                    labelMsg.setText("Error: " +e.getMessage());
                }
            }catch (Exception e){
                ;
            }
        }
    } // end of method

    @FXML
    protected void pressedButtonOpenSavedPdf()
    {
        // System.out.println("pressedButtonOpenSavedPdf");
        if (outputFile == null)
            return;
        try {
            Desktop.getDesktop().browse(outputFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
            labelMsg.setText("Error: " +e.getMessage());
        }
    }

    @FXML
    protected void pressedButtonGenerateOwnerPwd()
    {
        System.out.println("pressedButtonGenerateOwnerPwd");
        String strPwd = getPassWordString();
        if (strPwd != null && strPwd.trim().length()>0)
            textFieldOwnerWritePassword.setText(strPwd);
    }

    private String getPassWordString()
    {
        String ret = "";
        if (iGenPwdLen < 20) {
            iGenPwdLen = 20;
            textFieldGenPwdLen.setText("" + iGenPwdLen);
        }
        else
        if (iGenPwdLen > 99) {
            iGenPwdLen = 99;
            textFieldGenPwdLen.setText("" + iGenPwdLen);
        }
        String strPwd = PasswordGenerator.generateStrongPassword(iGenPwdLen);
        return strPwd;
    }

    @FXML
    protected void pressedButtonGenerateUserPwd()
    {
        System.out.println("pressedButtonGenerateUserPwd");
        String strPwd = getPassWordString();
        if (strPwd != null && strPwd.trim().length()>0)
            textFieldUserWritePassword.setText(strPwd);
    }

    /*
    @FXML
    protected void onHelloButtonClick() {
       // welcomeText.setText("Welcome to JavaFX Application!");
    }
     */
} // end of the class

            /*
            if (selectedFile.getAbsolutePath().equals(selectedFxmlFile.getAbsolutePath()))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning about existing file");
                alert.setHeaderText("The input file and output file are the same!");
                String s ="File: " +selectedFile.getAbsolutePath() +". The save is stopped now!";
                alert.setContentText(s);
                // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
                alert.show();
                return;
            }
            if (selectedFile.exists() && selectedFile.isFile() && selectedFile.length() != 0)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning about existing file");
                alert.setHeaderText("About JavaFx Player v 1.0");
                String s ="File: " +selectedFile.getAbsolutePath() +" existing all ready. Should it wll be over write?";
                alert.setContentText(s);
                // ObservableList<ButtonType> buttons = alert.getDialogPane().getButtonTypes();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        JavaFxmlFileConvetI18.writeIntoFile(selectedFile, textAreaFxml.getText().trim());
                        labelMsg.setText("Fxml variables was write into file: " +selectedFile.getAbsolutePath());
                    }catch (Exception e){
                        e.printStackTrace();
                        labelMsg.setText("Error in writing: " +e.getMessage());
                    }
                }
            }
            else
            {
                try {
                    JavaFxmlFileConvetI18.writeIntoFile(selectedFile, textAreaFxml.getText());
                    labelMsg.setText("Fxml variables was write into file: " +selectedFile.getAbsolutePath());
                }catch (Exception e){
                    e.printStackTrace();
                    labelMsg.setText("Error in writing: " +e.getMessage());
                }
            }
        }
*/