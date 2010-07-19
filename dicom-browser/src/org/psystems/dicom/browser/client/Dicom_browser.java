/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.browser.client;

import org.psystems.dicom.browser.client.component.BrowserPanel;
import org.psystems.dicom.browser.client.component.StudyManagePanel;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.service.BrowserService;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;
import org.psystems.dicom.browser.client.service.ManageStydyService;
import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dicom_browser implements EntryPoint {

	// Версия ПО (используется для проверки на стороне сервере при обновлении
	// клиента)
	public static String version = "0.1a";

	// Create a remote service proxy
	public final BrowserServiceAsync browserService = GWT
			.create(BrowserService.class);

	final ManageStydyServiceAsync manageStudyService = GWT
			.create(ManageStydyService.class);

	private DialogBox errorDialogBox;
	private HTML errorResponseLabel;

	// панель состояния работы запросов
	private PopupPanel workStatusPopup;
	private FlowPanel workStatusPanel;

	private VerticalPanel bodyPanel;

	public boolean showPageIntro = true;// Показ страницы с приглашением
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		_workStatusPopup();
		createErorrDlg();

		bodyPanel = new VerticalPanel();
		RootPanel.get("bodyContainer").add(bodyPanel);

//		BrowserPanel browserPanel = new BrowserPanel(this);
//		bodyPanel.add(browserPanel);

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

//				System.out.println("!!! " + event.getValue());
				if (event.getValue().equals("")) {

					bodyPanel.clear();
					BrowserPanel browserPanel = new BrowserPanel(
							Dicom_browser.this);
					bodyPanel.add(browserPanel);

				} else if (event.getValue().equals("newstudy")) {

					bodyPanel.clear();
					StudyManagePanel panel = new StudyManagePanel(
							manageStudyService);
					bodyPanel.add(panel);

				}
			}
		});
		
		History.fireCurrentHistoryState();

	}

	public void showErrorDlg(DefaultGWTRPCException e) {
		errorResponseLabel.setHTML("Ошибка: " + e.getText());
		errorDialogBox.show();
		errorDialogBox.center();

	}

	/**
	 * 
	 */
	private void createErorrDlg() {
		errorDialogBox = new DialogBox();
		errorDialogBox.setText("Ошибка!");
		errorDialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		errorResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(errorResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		errorDialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				errorDialogBox.hide();
			}
		});

	}

	/**
	 * создание диалога состояния поцесса работы
	 */
	private void _workStatusPopup() {

		workStatusPopup = new PopupPanel();
		workStatusPopup.hide();
		workStatusPopup.setStyleName("msgPopupPanel");
		// workStatusPanel.setAnimationEnabled(false);
		workStatusPanel = new FlowPanel();
		// workMsg = new HTML("");
		workStatusPanel.addStyleName("msgPopupPanelItem");
		workStatusPopup.add(workStatusPanel);
	}

	/**
	 * Показ панели состояния процесса
	 * 
	 * @param html
	 *            HTML сообщение
	 */
	public void showWorkStatusMsg(String html) {

		workStatusPanel.add(new HTML(html));
		workStatusPopuppopupCentering();
	}

	/**
	 * Центровка сообщения
	 */
	private void workStatusPopuppopupCentering() {
		workStatusPopup.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {

				workStatusPopup.setPopupPosition(offsetWidth, offsetHeight);
				int left = (Window.getClientWidth() - offsetWidth) >> 1;
				int top = 0;

				workStatusPopup.setPopupPosition(Window.getScrollLeft() + left,
						Window.getScrollTop() + top);
			}

		});

	}

	/**
	 * Добавление в сообщение строки
	 * 
	 * @param html
	 */
	public void addToWorkStatusMsg(String html) {
		workStatusPanel.add(new HTML(html));
		workStatusPopuppopupCentering();
	}

	/**
	 * Добавление в сообщение виджета
	 * 
	 * @param html
	 */
	public void addToWorkStatusWidget(Widget widget) {
		workStatusPanel.add(widget);
		workStatusPopuppopupCentering();
	}

	/**
	 * Скрытие панели состояния процесса
	 */
	public void hideWorkStatusMsg() {
		workStatusPanel.clear();
		workStatusPopup.hide();
	}

}
