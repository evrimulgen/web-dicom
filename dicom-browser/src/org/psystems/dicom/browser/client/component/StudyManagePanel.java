/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.TransactionTimer;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.EmployeeProxy;
import org.psystems.dicom.browser.client.proxy.ManufacturerDeviceProxy;
import org.psystems.dicom.browser.client.proxy.OOTemplateProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.PatientsRPCRequest;
import org.psystems.dicom.browser.client.proxy.PatientsRPCResponse;
import org.psystems.dicom.browser.client.proxy.Session;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dima_d
 * 
 *         Панель ввода-изменения исследования
 * 
 */
public class StudyManagePanel extends Composite implements ValueChangeHandler<String> {

    private StudyCard studyCardPanel;
    private HTML submitResult;
    private Button submitBtn;
    private TextBox patientName;
    private DateBox patientBirstdayDox;
    private DateBox studyDateBox;
    private FileUpload fileUpload;

    private FlexTable formTable;
    private VerticalPanel formDataPanel;
    // private Hidden patientBirthDateHidden;
    // private Hidden studyDateHidden;
    private TextBox medicalAlerts;
    private TextBox studyDescription;
    private TextArea studyComments;
    // private TextBox studyOperator;
    // private TextBox studyDoctror2;
    private DateBox studyViewProtocolDateBox;
    // private Hidden studyViewProtocolDateHidden;
    private ListBox patientNameCheck;
    protected HashMap<String, PatientProxy> itemProxies = new HashMap<String, PatientProxy>();
    private ListBox lbPatientSex;
    private StudyProxy proxy;
    // private HTML verifyHTML;
    private ListBox studyDoctror;
    private ListBox studyOperator;
    private int rowCounter; // TODO Убрать глобальность!
    // private Hidden studyInstanceUID;
    public final static String medicalAlertsTitle = "норма";

    private int timeClose = 5;
    private String msg;
    private TransactionTimer timer = null;
    // private ListBox studyManufacturerModelName;
    private ListBox studyModality;

    private HTML ooTemplatePanel = new HTML();
    private DiagnosisPanel diagnosisDirrectPanel;
    private ServicePanel servicesDirrectPanel;
    private DiagnosisPanel diagnosisPerformedPanel;
    private ServicePanel servicesPerformedPanel;
    private DicSuggestBox patientNameBox;
    private Label labelModality;

    // словари TODO Убрать! брать данные из индекса
    static TreeMap<String, String> dicModel = new TreeMap<String, String>();
    static HashMap<String, String> dicModality = new HashMap<String, String>();
    static HashMap<String, String> dicDoctors = new HashMap<String, String>();
    static {

	dicDoctors.put("Выберите врача...", "");
	dicDoctors.put("Петрова  Н.Н.", "Петрова  Н.Н.");
	dicDoctors.put("Девяткова И.А.", "Девяткова И.А.");
	dicDoctors.put("Солоница В.Д.", "Солоница В.Д.");
	dicDoctors.put("Корж С.С.", "Корж С.С.");
	dicDoctors.put("Кузнецова Е.А.", "Кузнецова Е.А.");
	dicDoctors.put("Лызлова И.Е", "Лызлова И.Е");
	dicDoctors.put("Шешеня Т.В.", "Шешеня Т.В.");
	dicDoctors.put("Тимошенко С.А.", "Тимошенко С.А.");
	dicDoctors.put("Батрак С.И.", "Батрак С.И.");
	dicDoctors.put("Леткина З.Ю.", "Леткина З.Ю.");
	dicDoctors.put("Перлова Е.В.", "Перлова Е.В.");
	dicDoctors.put("Сотиболдиев А.И.", "Сотиболдиев А.И.");
	dicDoctors.put("Сосновских Э.А.", "Сосновских Э.А.");
	dicDoctors.put("Зубкова Т.М.", "Зубкова Т.М.");

	dicModel.put("LORAD AFFINITY", "Маммограф (LORAD AFFINITY)");
	dicModel.put("CLINOMAT", "Рентген (CLINOMAT)");
	dicModel.put("РДК 50/6", "Рентген (РДК 50/6)");
	dicModel.put("DUODiagnost", "Рентген (DUODiagnost)");
	dicModel.put("FUJINON EG WR5", "Эндоскоп (FUJINON EG WR5)");
	dicModel.put("SonoScape-1", "УЗИ-1 (SonoScape) ДП 523 каб.");
	dicModel.put("SonoScape-2", "УЗИ-2 (SonoScape)");
	dicModel.put("SonoScape-3", "УЗИ-3 (SonoScape)");
	dicModel.put("VIVID 3", "УЗИ (VIVID) ДП 527 каб.");
	dicModel.put("УЗИ (VOLUSON)", "VOLUSON 730 BT04 EXPERT");
	dicModel.put("Acuson Sequoia", "УЗИ (Acuson Sequoia) каб.408");

	// TODO Уточнить имена и кабинеты
	dicModel.put("SSI-1000", "SSI-1000");
	dicModel.put("Эхоэнцефалоскоп-ЭЭС-25-ЭМА", "Эхоэнцефалоскоп-ЭЭС-25-ЭМА");
	dicModel.put("Электроэнцефалограф-Alliance", "Электроэнцефалограф-Alliance");
	dicModel.put("Companion III", "Companion III");
	dicModel.put("Microvit", "Microvit");
	dicModel.put("СМАД, Schiller AG", "СМАД, Schiller AG");
	dicModel.put("ЭКГ-Schiller Medical S.A.", "ЭКГ-Schiller Medical S.A.");
	dicModel.put("ЭКГ-Cardiovit AT-2 plus C", "ЭКГ-Cardiovit AT-2 plus C");
	dicModel.put("Велоэргометрия-АТ- 104 Schiller", "Велоэргометрия-АТ- 104 Schiller");
	dicModel.put("Voluson 730 Expert", "Voluson 730 Expert");
	dicModel.put("SSD-3500", "SSD-3500");
	dicModel.put("Спирометр-Spirovit SP-1", "Спирометр-Spirovit SP-1");
	dicModel.put("Спиро-Спектр 2", "Спиро-Спектр 2");
	dicModel.put("Aloka alfa", "Aloka alfa 6 каб.527 ДП");
	dicModel.put("Aloka 3500", "Aloka 3500 ВП 303 каб.");
	dicModality.put("OT", "Прочее");
	dicModality.put("CR", "Флюорография");
	dicModality.put("DF", "Рентген");
	dicModality.put("US", "Узи");
	dicModality.put("ES", "Эндоскопия");
	dicModality.put("MG", "Маммография");
    }

    /**
     * Установка и инициализация скрытых полей формы Нужно для upload-а картинок
     * и PDF-ок
     */
    private void setHiddenFields() {

	Hidden studyInstanceUID = new Hidden();
	studyInstanceUID.setName("0020000D");
	studyInstanceUID.setValue(proxy.getStudyInstanceUID());
	formDataPanel.add(studyInstanceUID);

	Hidden studySeriesUID = new Hidden();
	studySeriesUID.setName("0020000E");
	studySeriesUID.setValue(proxy.getStudyInstanceUID() + "." + new Date().getTime());
	formDataPanel.add(studySeriesUID);

	Hidden studyId = new Hidden();
	studyId.setName("00200010");
	studyId.setValue(proxy.getStudyId());
	formDataPanel.add(studyId);

	//

	Hidden patientId = new Hidden();
	patientId.setName("00100021");
	patientId.setValue(proxy.getPatientId());
	formDataPanel.add(patientId);

	Hidden patientName = new Hidden();
	patientName.setName("00100010");
	patientName.setValue(proxy.getPatientName());
	formDataPanel.add(patientName);

	Hidden patientSex = new Hidden();
	patientSex.setName("00100040");
	patientSex.setValue(proxy.getPatientSex());
	formDataPanel.add(patientSex);

	Hidden patientBirthDateHidden = new Hidden();
	patientBirthDateHidden.setName("00100030");
	patientBirthDateHidden.setValue(proxy.getPatientBirthDate());
	formDataPanel.add(patientBirthDateHidden);

	//

	Hidden manufacturerModelName = new Hidden();
	manufacturerModelName.setName("00081090");
	manufacturerModelName.setValue(proxy.getManufacturerModelName());
	formDataPanel.add(manufacturerModelName);

	Hidden modality = new Hidden();
	modality.setName("00080060");
	modality.setValue(proxy.getStudyModality());
	formDataPanel.add(modality);

	Hidden studyDateHidden = new Hidden();
	studyDateHidden.setName("00080020");
	studyDateHidden.setValue(proxy.getStudyDate());
	formDataPanel.add(studyDateHidden);

	Hidden studyViewProtocolDateHidden = new Hidden();
	studyViewProtocolDateHidden.setName("00321050");
	studyViewProtocolDateHidden.setValue(proxy.getStudyViewprotocolDate());
	formDataPanel.add(studyViewProtocolDateHidden);

    }

    /**
     * @param proxy
     */
    private void setProxy(StudyProxy proxy) {
	this.proxy = proxy;

	// Инициаизация полей
    }

    /**
     * Установка значений контролов по пациенту
     */
    private void setPatientControls() {
	if (proxy.getPatientName() != null) {
	    if ("F".equals(proxy.getPatientSex())) {
		lbPatientSex.setSelectedIndex(1);
	    } else {
		lbPatientSex.setSelectedIndex(0);
	    }
	    patientBirstdayDox.setValue(Utils.dateFormatSql.parse(proxy.getPatientBirthDate()));
	} else {
	    lbPatientSex.setSelectedIndex(-1);
	    patientBirstdayDox.setValue(null);
	}
    }

    /**
     * Установка значений контролов по модальности
     */
    private void setModalityControls() {

	if (proxy.getDirection() != null && proxy.getDirection().getDevice() != null)
	    labelModality.setText(proxy.getDirection().getDevice().getModality());
	else
	    labelModality.setText(proxy.getStudyModality());
    }

    /**
     * 
     * @param studyCardPanel
     * @param proxy
     *            FIXME Убрать этот аргумент !!!
     */
    public StudyManagePanel(StudyCard studyCardPanel, final StudyProxy proxy) {

	this.studyCardPanel = studyCardPanel;
	setProxy(proxy);

	// История
	History.addValueChangeHandler(this);
	// History.fireCurrentHistoryState();

	DecoratorPanel mainPanel = new DecoratorPanel();

	final FormPanel formPanel = new FormPanel();
	formPanel.setAction("newstudy/upload");
	formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	formPanel.setMethod(FormPanel.METHOD_POST);
	mainPanel.setWidget(formPanel);

	formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

	    @Override
	    public void onSubmitComplete(SubmitCompleteEvent event) {

		// TODO Сделать через анализ статуса ответа (200 - ОК)
		if (!event.getResults().matches(".+___success___.+")) {
		    submitError(event);
		} else {
		    submitSuccess();
		}
	    }

	});

	formTable = new FlexTable();
	// TODO Убрать в css
	DOM.setStyleAttribute(formPanel.getElement(), "background", "#E9EDF5");

	HorizontalPanel hpMain = new HorizontalPanel();
	hpMain.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
	hpMain.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

	formDataPanel = new VerticalPanel();

	formPanel.add(hpMain);
	hpMain.add(formDataPanel);
	formDataPanel.add(formTable);

	rowCounter = 0;

	if (proxy.getDirection() != null) {
	    addFormRow(rowCounter++, "Ввод по ", new Label("Направлению"));
	} else {
	    addFormRow(rowCounter++, "Ввод по ", new Label("Фамилии или c аппарата"));
	}

	// *********************************************************************************
	// Пациент
	// *********************************************************************************

	if (proxy.getDirection() != null) {
	    addFormRow(rowCounter++, "Пациент", new Label(proxy.getPatientName() + " (из направления)"));
	} else {

	    patientNameBox = new DicSuggestBox("patients");
	    patientNameBox.setWidth("400px");

	    if (proxy.getPatientName() != null)
		patientNameBox.getSuggestBox().setText(proxy.getPatientName());

	    patientNameBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {

		    ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		    PatientProxy patient = (PatientProxy) item.getEvent();
		    proxy.setPatientName(patient.getPatientName());
		    proxy.setPatientSex(patient.getPatientSex());
		    proxy.setPatientBirthDate(patient.getPatientBirthDate());
		    proxy.setPatientId(patient.getPatientId());
		    setPatientControls();
		}

	    });

	    patientNameBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

		@Override
		public void onBlur(BlurEvent event) {

		    if (proxy.getPatientName() != null)
			patientNameBox.getSuggestBox().setText(proxy.getPatientName());

		    else
			patientNameBox.getSuggestBox().setText("");

		    setPatientControls();
		}
	    });

	    addFormRow(rowCounter++, "ФИО", patientNameBox);

	    lbPatientSex = new ListBox();
	    lbPatientSex.addItem("Муж", "M");
	    lbPatientSex.addItem("Жен", "F");

	    addFormRow(rowCounter++, "Пол", lbPatientSex);

	    //
	    patientBirstdayDox = new DateBox();
	    patientBirstdayDox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	    patientBirstdayDox.addValueChangeHandler(new ValueChangeHandler<Date>() {

		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
		    String d = Utils.dateFormatDicom.format(event.getValue());
		    proxy.setPatientBirthDate(d);
		}
	    });

	    addFormRow(rowCounter++, "Дата рождения", patientBirstdayDox);

	    setPatientControls();

	}

	addFormRow(rowCounter++, "StudyId", new Label(proxy.getStudyId()));

	// *********************************************************************************
	// Аппарат
	// *********************************************************************************

	if (proxy.getDirection() != null) {
	    addFormRow(rowCounter++, "Направлен на аппарт", new Label(proxy.getDirection().getDevice()
		    .getManufacturerModelName()));
	} else {

	    final DicSuggestBox deviceBox = new DicSuggestBox("devices");

	    if (proxy.getManufacturerModelName() != null)
		deviceBox.getSuggestBox().setText(proxy.getManufacturerModelName());

	    deviceBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
		    ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		    ManufacturerDeviceProxy dev = (ManufacturerDeviceProxy) item.getEvent();
		    proxy.setManufacturerModelName(dev.getManufacturerModelName());
		    proxy.setStudyModality(dev.getModality());
		    setModalityControls();
		}
	    });

	    deviceBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

		@Override
		public void onBlur(BlurEvent event) {
		    if (proxy.getManufacturerModelName() != null)
			deviceBox.getSuggestBox().setText(proxy.getManufacturerModelName());
		    else
			deviceBox.getSuggestBox().setText("");

		    setModalityControls();
		}
	    });

	    addFormRow(rowCounter++, "Аппарат", deviceBox);

	}

	// Модальность

	labelModality = new Label("");
	setModalityControls();

	addFormRow(rowCounter++, "Тип исследования", labelModality);

	// *********************************************************************************
	// Дата исследования
	// *********************************************************************************

	studyDateBox = new DateBox();
	studyDateBox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));

	if (proxy.getStudyDate() == null) {
	    studyDateBox.setValue(new Date());
	} else {
	    studyDateBox.setValue(Utils.dateFormatSql.parse(proxy.getStudyDate()));
	}
	studyDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		proxy.setStudyDate(Utils.dateFormatDicom.format(event.getValue()));
	    }
	});

	addFormRow(rowCounter++, "Дата исследования", studyDateBox);

	//
	studyViewProtocolDateBox = new DateBox();
	studyViewProtocolDateBox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	studyViewProtocolDateBox.setValue(new Date());
	studyViewProtocolDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		proxy.setStudyViewprotocolDate(Utils.dateFormatDicom.format(event.getValue()));
	    }
	});

	addFormRow(rowCounter++, "Дата описания", studyViewProtocolDateBox);

	studyDoctror = new ListBox();
	studyDoctror.setName("00080090");
	// TODO Вынести в конфиг!!!
	for (Iterator<String> iter = dicDoctors.keySet().iterator(); iter.hasNext();) {
	    String key = iter.next();
	    String val = dicModel.get(key);
	    studyDoctror.addItem(key, val);
	}

	// TODO Переделать проверку на ввод из словаря
	boolean find = false;
	for (int i = 0; i < studyDoctror.getItemCount(); i++) {
	    String item = studyDoctror.getItemText(i);
	    if (item.equalsIgnoreCase(proxy.getStudyDoctor())) {
		studyDoctror.setSelectedIndex(i);
		find = true;
		break;
	    }
	}

	if (!find && proxy.getStudyDoctor() != null && proxy.getStudyDoctor().length() != 0) {
	    studyDoctror.addItem(proxy.getStudyDoctor() + " (нет в словаре!)", proxy.getStudyDoctor());
	    studyDoctror.setSelectedIndex(studyDoctror.getItemCount() - 1);
	}

	addFormRow(rowCounter++, "Врач", studyDoctror);

	// TODO Вынести в конфиг!!!
	studyOperator = new ListBox();
	studyOperator.setName("00081070");
	studyOperator.addItem("Выберите лаборанта...", "");
	studyOperator.addItem("Михеева И.А.", "Михеева И.А.");
	studyOperator.addItem("Тебенев Е.Н.", "Тебенев Е.Н.");
	studyOperator.addItem("Диденко В.А.", "Диденко В.А.");

	studyOperator.addItem("Серенина Е.И.", "Серенина Е.И.");
	studyOperator.addItem("Кайдалова Ю.А.", "Кайдалова Ю.А.");
	studyOperator.addItem("Серенко Л.Е.", "Серенко Л.Е.");
	studyOperator.addItem("Гиниатуллина Г.Н.", "Гиниатуллина Г.Н.");
	studyOperator.addItem("Собирова Г.К.", "Собирова Г.К.");
	studyOperator.addItem("Иванова О.И.", "Иванова О.И.");
	studyOperator.addItem("Бабушкина Л.В.", "Бабушкина Л.В.");
	studyOperator.addItem("Иригбаева А.Э.", "Иригбаева А.Э.");

	studyOperator.addItem("Хозяшева Д.А.", "Хозяшева Д.А.");
	studyOperator.addItem("Донских А.И.", "Донских А.И.");
	studyOperator.addItem("Букина Б.Б.", "Букина Б.Б.");
	// studyOperator2.addItem("Ввести нового...", "manualinput");

	find = false;
	for (int i = 0; i < studyOperator.getItemCount(); i++) {
	    String item = studyOperator.getItemText(i);
	    if (item.equalsIgnoreCase(proxy.getStudyOperator())) {
		studyOperator.setSelectedIndex(i);
		find = true;
		break;
	    }
	}

	if (!find && proxy.getStudyOperator() != null && proxy.getStudyOperator().length() != 0) {
	    studyOperator.addItem(proxy.getStudyOperator() + " (нет в словаре!)", proxy.getStudyOperator());
	    studyOperator.setSelectedIndex(studyOperator.getItemCount() - 1);
	}

	// studyOperator2.addChangeHandler(new ChangeHandler() {
	//
	// @Override
	// public void onChange(ChangeEvent event) {
	// // TODO Auto-generated method stub
	// // System.out.println("!!! "+event)!!!;
	// // int i = studyOperator2.getSelectedIndex();
	// // studyOperator2.getValue(i);
	// }
	// });

	addFormRow(rowCounter++, "Лаборант", studyOperator);

	//
	// TODO Взять из конфигурации
	final ListBox lbDescriptionTemplates = new ListBox();
	// lbDescriptionTemplates.setName("00100040");
	lbDescriptionTemplates.addItem("Выберите шаблон...", "");
	lbDescriptionTemplates.addItem("Флюорография, Прямая передняя", "Флюорография, Прямая передняя");

	lbDescriptionTemplates.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		// System.out.println("!!! "+event)!!!;
		int i = lbDescriptionTemplates.getSelectedIndex();
		studyDescription.setText(lbDescriptionTemplates.getValue(i));
	    }
	});

	addFormRow(rowCounter++, "Варианты описания", lbDescriptionTemplates);
	//
	studyDescription = new TextBox();
	studyDescription.setName("00081030");
	studyDescription.setWidth("400px");
	studyDescription.setText(proxy.getStudyDescription());
	addFormRow(rowCounter++, "Описание исследования", studyDescription);

	//
	medicalAlerts = new TextBox();
	medicalAlerts.setName("00102000");
	medicalAlerts.setWidth("400px");

	// medicalAlerts.addStyleName("DicomSuggestionEmpty");
	medicalAlerts.setTitle(medicalAlertsTitle);
	medicalAlerts.setText(medicalAlertsTitle);

	medicalAlerts.setText(proxy.getStudyResult());
	if (proxy.getStudyResult() == null || proxy.getStudyResult().length() == 0) {
	    medicalAlerts.setText(medicalAlertsTitle);
	}

	if (proxy.getStudyResult() != null && proxy.getStudyResult().length() > 0) {
	    // medicalAlerts.removeStyleName("DicomSuggestionEmpty");
	    // medicalAlerts.addStyleName("DicomSuggestion");
	}
	addFormRow(rowCounter++, "Результат", medicalAlerts);

	medicalAlerts.addFocusHandler(new FocusHandler() {

	    @Override
	    public void onFocus(FocusEvent event) {

		// medicalAlerts.removeStyleName("DicomSuggestionEmpty");
		// medicalAlerts.addStyleName("DicomSuggestion");

		if (medicalAlerts.getText().equals(medicalAlerts.getTitle())) {
		    medicalAlerts.setValue("");
		} else {
		    medicalAlerts.setValue(medicalAlerts.getValue());
		}
	    }

	});

	medicalAlerts.addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {

		if (medicalAlerts.getText().equals("")) {
		    medicalAlerts.setValue(medicalAlerts.getTitle());
		    // medicalAlerts.removeStyleName("DicomSuggestion");
		    // medicalAlerts.addStyleName("DicomSuggestionEmpty");
		} else {
		    medicalAlerts.setValue(medicalAlerts.getValue());
		}

	    }

	});

	//
	final ListBox lbCommentsTemplates = new ListBox();
	// lbCommentsTemplates.setName("00100040");
	lbCommentsTemplates.addItem("Выберите шаблон...", "");
	lbCommentsTemplates.addItem("Органы грудной клетки без видимой патологии",
		"Органы грудной клетки без видимой патологии");

	lbCommentsTemplates.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		// System.out.println("!!! "+event)!!!;
		int i = lbCommentsTemplates.getSelectedIndex();
		studyComments.setText(lbCommentsTemplates.getValue(i));
	    }
	});

	addFormRow(rowCounter++, "варианты протокола", lbCommentsTemplates);

	//
	studyComments = new TextArea();
	studyComments.setName("00324000");// Tag.StudyComments
	studyComments.setSize("400px", "200px");
	studyComments.setText(proxy.getStudyViewprotocol());
	addFormRow(rowCounter++, "Протокол", studyComments);

	//		
	//
	fileUpload = new FileUpload();
	fileUpload.setName("upload");

	ListBox lbAttachnebtType = new ListBox();
	lbAttachnebtType.setName("content_type");
	lbAttachnebtType.addItem("Описание", "application/pdf");
	lbAttachnebtType.addItem("Снимок", "image/jpg");

	addFormRow(rowCounter++, lbAttachnebtType, fileUpload);

	//
	submitBtn = new Button("Сохранить изменения...");
	dataVerifyed(false);
	submitBtn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		// TODO Сделать подсветки виджета

		// if (studyManufacturerModelName != null &&
		// studyManufacturerModelName.getSelectedIndex() <= 0) {
		// Window.alert("Выберите аппарат!");
		// studyManufacturerModelName.setFocus(true);
		// return;
		// }

		// TODO Сделать подсветки виджета
		if (studyModality != null && studyModality.getSelectedIndex() <= 0) {
		    Window.alert("Выберите тип исследования!");
		    studyModality.setFocus(true);
		    return;
		}

		// TODO Сделать недоступной кнопку "Сохранить"
		submitResult.setHTML("сохранение...");
		submitBtn.setEnabled(false);
		formPanel.submit();

	    }
	});
	addFormRow(rowCounter++, submitBtn);

	// BEGIN данные из направления

	if (proxy.getDirection() != null) {

	    final DirectionProxy direction = proxy.getDirection();

	    addFormRow(rowCounter++, "Направление №", new Label(direction.getDirectionId() + "(" + direction.getId()
		    + ")"));

	    //
	    final TextBox directionLoction = new TextBox();
	    directionLoction.setText(direction.getDirectionLocation());
	    directionLoction.addChangeHandler(new ChangeHandler() {

		@Override
		public void onChange(ChangeEvent event) {
		    // TODO Auto-generated method stub
		    direction.setDirectionLocation(directionLoction.getText());
		}
	    });
	    addFormRow(rowCounter++, "Размещение", directionLoction);

	    //
	    final TextBox directionCode = new TextBox();
	    directionCode.setText(direction.getDirectionCode());
	    directionCode.addChangeHandler(new ChangeHandler() {

		@Override
		public void onChange(ChangeEvent event) {
		    // TODO Auto-generated method stub
		    direction.setDirectionCode(directionCode.getText());
		}
	    });
	    addFormRow(rowCounter++, "Идент.случая", directionCode);

	    //
	    DateBox dateDirection = new DateBox();
	    dateDirection.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));

	    dateDirection.addValueChangeHandler(new ValueChangeHandler<Date>() {

		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
		    String d = Utils.dateFormatSql.format(event.getValue());
		    direction.setDateDirection(d);
		}
	    });

	    dateDirection.setValue(Utils.dateFormatSql.parse(direction.getDateDirection()));
	    addFormRow(rowCounter++, "Дата направления", dateDirection);

	    //
	    final DicSuggestBox doctorDirrectedBox = new DicSuggestBox("doctors");
	    if (direction.getDoctorDirect() != null)
		doctorDirrectedBox.getSuggestBox().setText(direction.getDoctorDirect().getEmployeeName());

	    doctorDirrectedBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
		    ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		    EmployeeProxy doctor = (EmployeeProxy) item.getEvent();
		    direction.setDoctorDirect(doctor);
		}
	    });
	    doctorDirrectedBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

		@Override
		public void onBlur(BlurEvent event) {
		    if (direction.getDoctorDirect() != null)
			doctorDirrectedBox.getSuggestBox().setText(direction.getDoctorDirect().getEmployeeName());
		    else
			doctorDirrectedBox.getSuggestBox().setText("");
		}
	    });
	    addFormRow(rowCounter++, "Направивший врач", doctorDirrectedBox);

	    //
	    diagnosisDirrectPanel = new DiagnosisPanel();
	    diagnosisDirrectPanel.setDiagnosis(direction.getDiagnosisDirect());
	    addFormRow(rowCounter++, "Направленные диагнозы", diagnosisDirrectPanel);

	    //
	    servicesDirrectPanel = new ServicePanel();
	    servicesDirrectPanel.setServices(direction.getServicesDirect());
	    addFormRow(rowCounter++, "Направленные услуги", servicesDirrectPanel);

	    //
	    final DicSuggestBox doctorPerformedBox = new DicSuggestBox("doctors");
	    if (direction.getDoctorPerformed() != null)
		doctorPerformedBox.getSuggestBox().setText(direction.getDoctorPerformed().getEmployeeName());

	    doctorPerformedBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
		    ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		    EmployeeProxy doctor = (EmployeeProxy) item.getEvent();
		    direction.setDoctorPerformed(doctor);
		}
	    });

	    doctorPerformedBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

		@Override
		public void onBlur(BlurEvent event) {
		    if (direction.getDoctorPerformed() != null)
			doctorPerformedBox.getSuggestBox().setText(direction.getDoctorPerformed().getEmployeeName());
		    else
			doctorPerformedBox.getSuggestBox().setText("");
		}
	    });

	    addFormRow(rowCounter++, "Принимающий врач", doctorPerformedBox);

	    //
	    diagnosisPerformedPanel = new DiagnosisPanel();
	    diagnosisPerformedPanel.setDiagnosis(direction.getDiagnosisPerformed());
	    addFormRow(rowCounter++, "Подтвержденные диагнозы", diagnosisPerformedPanel);

	    //
	    servicesPerformedPanel = new ServicePanel();
	    servicesPerformedPanel.setServices(direction.getServicesPerformed());
	    addFormRow(rowCounter++, "Подтвержденные услуги", servicesPerformedPanel);

	    //
	    DateBox datePerformed = new DateBox();
	    datePerformed.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));

	    datePerformed.addValueChangeHandler(new ValueChangeHandler<Date>() {

		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
		    String d = Utils.dateFormatSql.format(event.getValue());
		    direction.setDatePerformed(d);
		}
	    });

	    if (direction.getDatePerformed() != null)
		datePerformed.setValue(Utils.dateFormatSql.parse(direction.getDatePerformed()));
	    addFormRow(rowCounter++, "Дата выполнения", datePerformed);

	    //
	    addFormRow(rowCounter++, "Планируемая дата", new Label(direction.getDateTimePlanned()));

	    //
	    final Button btn = new Button("Сохранить направление");
	    btn.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {

		    // proxy.getDirection().setDirectionCode("CODEтест111");
		    btn.setEnabled(false);
		    // diagnosisDirrectPanel.getDiagnosis();
		    proxy.getDirection().setDiagnosisDirect(diagnosisDirrectPanel.getDiagnosis());
		    proxy.getDirection().setServicesDirect(servicesDirrectPanel.getServices());
		    proxy.getDirection().setDiagnosisPerformed(diagnosisPerformedPanel.getDiagnosis());
		    proxy.getDirection().setServicesPerformed(servicesPerformedPanel.getServices());

		    Dicom_browser.browserService.saveDirection(proxy.getDirection(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
			    btn.setEnabled(true);
			    Dicom_browser.showErrorDlg(caught);

			}

			@Override
			public void onSuccess(Void result) {
			    btn.setEnabled(true);
			}
		    });
		}
	    });

	    addFormRow(rowCounter++, btn);
	}

	// END данные из направления

	//
	submitResult = new HTML();

	addFormRow(rowCounter++, submitResult);

	// Добавление правой части панели

	// formTable.getFlexCellFormatter().setRowSpan(0, 2, 17);
	int row = 0;

	VerticalPanel vpRight = new VerticalPanel();
	vpRight.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
	vpRight.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
	formTable.setWidget(row, 2, vpRight);

	hpMain.add(vpRight);

	// vpRight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	// formTable.getCellFormatter().setHorizontalAlignment(row, 2,
	// VerticalPanel.ALIGN_LEFT);
	// formTable.getCellFormatter().setVerticalAlignment(row, 2,
	// VerticalPanel.ALIGN_TOP);

	vpRight.add(makeItemLabel("Сверка имени (Click - выбор)"));
	vpRight.add(patientNameCheck);

	vpRight.add(makeItemLabel("Шаблоны"));
	ooTemplatePanel.setHTML("загрузка шаблонов...");
	vpRight.add(ooTemplatePanel);

	if (proxy != null && proxy.getId() > 0) {
	    initTemplates(proxy.getStudyModality());
	} else {
	    ooTemplatePanel.setHTML("Прикрепление шаблона доступно только в существующем исследовании");
	}

	formTable.getFlexCellFormatter().setRowSpan(0, 2, rowCounter);

	// System.out.println("!!! rowCounter="+rowCounter);
	// formTable.getFlexCellFormatter().setRowSpan(rowCounter, 1 , 17);

	//
	updateFromSession();

	initWidget(mainPanel);

	patientVerify();
    }

    /**
     * Обновление из сессии
     */
    private void updateFromSession() {

	submitResult.setHTML("Загрузка конфигурации...");
	submitBtn.setEnabled(false);

	Dicom_browser.browserService.getSessionObject(new AsyncCallback<Session>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Dicom_browser.showErrorDlg(caught);
		submitResult.setHTML("Ошибка загрузка конфигурации!");
		submitBtn.setEnabled(true);
	    }

	    @Override
	    public void onSuccess(Session result) {

		submitResult.setHTML("");
		// System.out.println("!!!! Session result="+result);
		submitBtn.setEnabled(true);
		if (result == null)
		    return;

		String ManufacturerModelName = result.getStudyManagePanel_ManufacturerModelName();
		String Modality = result.getStudyManagePanel_Modality();

		// System.out.println("!!!! getStudyManagePanel_ManufacturerModelName="+result.getStudyManagePanel_ManufacturerModelName());
		// System.out.println("!!!! getStudyManagePanel_Modality="+result.getStudyManagePanel_Modality());

		// for (int i = 0; i <
		// studyManufacturerModelName.getItemCount(); i++) {
		// if
		// (studyManufacturerModelName.getValue(i).equals(ManufacturerModelName))
		// {
		// studyManufacturerModelName.setSelectedIndex(i);
		// break;
		// }
		// }

		for (int i = 0; i < studyModality.getItemCount(); i++) {
		    if (studyModality.getValue(i).equals(Modality)) {
			studyModality.setSelectedIndex(i);
			break;
		    }
		}

	    }
	});

	// Dicom_browser.browserService.getDcmTagsFromFile(0,
	// Dicom_browser.version, proxy.getId(),
	// new AsyncCallback<ArrayList<DcmTagProxy>>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// vp.clear();
	// vp.add(new Label("Ошибка полчения данных! " + caught));
	// }
	//
	// @Override
	// public void onSuccess(ArrayList<DcmTagProxy> result) {
	// vp.clear();
	// for (Iterator<DcmTagProxy> it = result.iterator(); it
	// .hasNext();) {
	// DcmTagProxy proxy = it.next();
	// vp.add(new Label("" + proxy));
	// }
	// }
	// });
    }

    protected void dataVerifyed(boolean b) {

	if (b) {
	    submitBtn.setText("Сохранить изменения...");
	} else {
	    submitBtn.setText("Сохранить НЕПРОВЕРЕННЫЕ изменения...");
	}
    }

    private void patientVerify() {

	if (patientName == null)
	    return;

	patientNameCheck.addItem("Ищем: " + patientName.getValue());
	dataVerifyed(false);

	PatientsRPCRequest req = new PatientsRPCRequest();
	req.setTransactionId(1);
	req.setQueryStr(patientName.getText() + "%");
	req.setLimit(20);

	Dicom_browser.browserService.getPatients(req, new AsyncCallback<PatientsRPCResponse>() {

	    private Object patientProxy;

	    public void onFailure(Throwable caught) {

		// transactionFinished();
		// Dicom_browser.showErrorDlg(caught);
		patientNameCheck.clear();
		patientNameCheck.addItem("Ошибка поиска из внешней системы!");
		VerticalPanel panel = (VerticalPanel) patientNameCheck.getParent();

		// HTML msg = new HTML();
		TextArea msg = new TextArea();
		msg.setSize("450px", "20em");
		if (caught instanceof DefaultGWTRPCException) {
		    DefaultGWTRPCException ex = (DefaultGWTRPCException) caught;
		    msg.setText("Ошибка поиска из внешней системы !!!! \n" + ex.getMessage() + "\n["
			    + ex.getLogMarker() + "]\n" + ex.getStack() + "</pre>");
		} else {
		    msg.setText(caught.getMessage());
		}

		panel.add(msg);

	    }

	    public void onSuccess(PatientsRPCResponse result) {

		// TODO попробовать сделать нормлаьный interrupt (дабы
		// не качать все данные)

		// Если сменился идентификатор транзакции, то ничего не
		// принимаем
		// if (searchTransactionID != result.getTransactionId())
		// {
		// return;
		// }

		Dicom_browser.hideWorkStatusMsg();

		ArrayList<PatientProxy> patients = result.getPatients();
		patientNameCheck.clear();
		itemProxies = new HashMap<String, PatientProxy>();
		itemProxies.clear();

		PatientProxy lastPatientProxy = null;
		for (Iterator<PatientProxy> it = patients.iterator(); it.hasNext();) {

		    PatientProxy patientProxy = it.next();
		    lastPatientProxy = patientProxy;
		    String sex;
		    if ("M".equals(patientProxy.getPatientSex())) {
			sex = "М";
		    } else {
			sex = "Ж";
		    }
		    // String d =
		    // Utils.dateFormatDicom.format(event.getValue());

		    patientNameCheck.addItem(patientProxy.getPatientName()
			    + " ("
			    + sex
			    + ") "
			    + Utils.dateFormatUser
				    .format(Utils.dateFormatSql.parse(patientProxy.getPatientBirthDate())), ""
			    + patientProxy.getId());

		    itemProxies.put("" + patientProxy.getId(), patientProxy);
		    patientNameCheck.setSelectedIndex(0);
		}

		if (patients.size() == 0) {
		    patientNameCheck.addItem("Совпадений не найдено!");
		    dataVerifyed(false);

		}

		if (patients.size() == 1) {

		    applyVerifyedData(lastPatientProxy);
		}

		// transactionFinished();

	    }

	});

    }

    /**
     * Применение сверенных данных
     * 
     * @param lastPatientProxy
     */
    protected void applyVerifyedData(PatientProxy lastPatientProxy) {
	if (lastPatientProxy == null)
	    return;
	patientName.setValue(lastPatientProxy.getPatientName());

	patientBirstdayDox.setValue(Utils.dateFormatSql.parse(lastPatientProxy.getPatientBirthDate()));
	if ("M".equals(lastPatientProxy.getPatientSex()))
	    lbPatientSex.setSelectedIndex(0);
	else
	    lbPatientSex.setSelectedIndex(1);
	dataVerifyed(true);
    }

    /**
     * Добавление строчки на форму
     * 
     * @param row
     * @param title
     * @param input
     */
    private void addFormRow(int row, String title, Widget input) {

	formTable.setWidget(row, 0, makeItemLabel(title));
	formTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	formTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
	formTable.setWidget(row, 1, input);

    }

    /**
     * Добавление строчки на форму
     * 
     * @param row
     * @param title
     * @param input
     */
    private void addFormRow(int row, Widget title, Widget input) {

	formTable.setWidget(row, 0, title);
	formTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	formTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
	formTable.setWidget(row, 1, input);

    }

    /**
     * Добавление строчки на форму
     * 
     * @param row
     * @param input
     */
    private void addFormRow(int row, Widget input) {

	formTable.setWidget(row, 0, input);
	formTable.getFlexCellFormatter().setColSpan(row, 0, 2);
	formTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);

    }

    private void clearForm() {
	dataVerifyed(true);
	resetForm();
	studyDateBox.setValue(new Date());
    }

    private native void resetForm() /*-{
		$doc.forms[0].reset();
    }-*/;

    /**
     * НЕуспешное завершение сохранения исследования
     */
    protected void submitError(SubmitCompleteEvent event) {
	submitResult.setHTML("" + event.toDebugString() + "<HR>" + event.getResults());
	submitBtn.setEnabled(true);
	// dataVerifyed(true);
    }

    /**
     * Успешное завершение сохранения исследования
     */
    protected void submitSuccess() {
	// clearForm();

	Dicom_browser.browserService.getStudyByID(1, Dicom_browser.version, StudyManagePanel.this.proxy.getId(),
		new AsyncCallback<StudyProxy>() {

		    @Override
		    public void onFailure(Throwable caught) {
			Dicom_browser.showErrorDlg(caught);
		    }

		    @Override
		    public void onSuccess(StudyProxy result) {

			if (studyCardPanel != null) {
			    if (result == null) {
				Dicom_browser.showErrorDlg(new RuntimeException("Данные не найдены! id исследования: "
					+ StudyManagePanel.this.proxy.getId()));
				submitResult.setHTML("Данные не найдены! id исследования: "
					+ StudyManagePanel.this.proxy.getId());
				return;
			    }
			    studyCardPanel.setProxy(result);
			}

			msg = "Исследование изменено. Данные успешно сохранены.";
			if (studyCardPanel == null) {
			    msg = "Создано новое исследование.";
			    timeClose = 10;
			}

			// submitBtn.setEnabled(true);
			submitBtn.removeFromParent();

			Button closeBtn = new Button("Закрыть форму ввода");
			closeBtn.addClickHandler(new ClickHandler() {

			    @Override
			    public void onClick(ClickEvent event) {
				StudyManagePanel.this.removeFromParent();
				timer.cancel();
			    }
			});

			addFormRow(rowCounter++, closeBtn);

			timer = new TransactionTimer() {
			    int counter = 0;

			    @Override
			    public void run() {

				submitResult.setHTML(msg + " форма автоматически закроется через <b>"
					+ (timeClose - counter) + "</b> секунд(ы)");
				if (counter++ >= timeClose) {

				    StudyManagePanel.this.removeFromParent();
				}
			    }

			};

			timer.scheduleRepeating(1000);

		    }

		});

    }

    /**
     * Получение списка шаблонов
     * 
     * @param modality
     * @return
     */
    void initTemplates(String modality) {

	Dicom_browser.browserService.getOOTemplates(modality, new AsyncCallback<ArrayList<OOTemplateProxy>>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Dicom_browser.showErrorDlg(caught);
	    }

	    @Override
	    public void onSuccess(ArrayList<OOTemplateProxy> result) {

		String tmpls = "";
		for (int i = 0; i < result.size(); i++) {
		    tmpls += "<a href='" + result.get(i).getUrl() + "?id=" + proxy.getId() + "'>"
			    + result.get(i).getTitle() + "</a><br>";
		}

		// for(int i=0; i<100; i++) {
		// tmpls += "<a href='"+"?id="+proxy.getId()+"'>" + i
		// +"</a><br>";
		// }

		ooTemplatePanel.setHTML(tmpls);
	    }
	});

    }

    /**
     * @param title
     * @return
     */
    Widget makeItemLabel(String title) {
	Label label = new Label(title);
	// TODO Убрать в css
	DOM.setStyleAttribute(label.getElement(), "font", "1.5em/ 150% normal Verdana, Tahoma, sans-serif");
	return label;
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
	// TODO Auto-generated method stub

    }

}
