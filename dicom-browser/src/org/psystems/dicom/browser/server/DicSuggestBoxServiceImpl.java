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
package org.psystems.dicom.browser.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DiagnosisProxy;
import org.psystems.dicom.browser.client.proxy.ServiceProxy;
import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;
import org.psystems.dicom.browser.client.service.DicSuggestBoxService;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DicSuggestBoxServiceImpl extends RemoteServiceServlet implements
		DicSuggestBoxService {

	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger
			.getLogger(DicSuggestBoxServiceImpl.class.getName());

	public SuggestTransactedResponse getSuggestions(long transactionId,
			String version, String dicName, SuggestOracle.Request req)
			throws DefaultGWTRPCException {
		SuggestTransactedResponse resp = new SuggestTransactedResponse();
		resp.setTransactionId(transactionId);

		// проверка версии клиента
		org.psystems.dicom.browser.server.Util.checkClentVersion(version);
		
		// Create a list to hold our suggestions (pre-set the lengthto the limit
		// specified by the request)

		
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		
	try {

	    // getServletContext(), req.getQuery(), req.getLimit()
	    if (dicName.equals("diagnosis")) {
		
		for (int i = 0; i < 10; i++) {
		    DiagnosisProxy proxy = new DiagnosisProxy();
		    proxy.setDiagnosisCode(req.getQuery() + i);
		    proxy.setDiagnosisDescription(req.getQuery() + i + " Диагноз тестовый");
		    
		    ItemSuggestion item = new ItemSuggestion("ищем "+proxy.getDiagnosisDescription() + "...",
			    proxy.getDiagnosisCode().toUpperCase());
		    item.setEvent(proxy);
		    suggestions.add(item);
		}
	    } else if (dicName.equals("services")) {
		
		for (int i = 0; i < 10; i++) {
		    ServiceProxy proxy = new ServiceProxy();
		    proxy.setServiceCode(req.getQuery() + i);
		    proxy.setServiceAlias("alias"+i);
		    proxy.setServiceDescription(req.getQuery() + i + " услуга тестовая");
		    proxy.setServiceCount(1);
		    
		    ItemSuggestion item = new ItemSuggestion("ищем "+proxy.getServiceDescription() + "...",
			    proxy.getServiceCode().toUpperCase());
		    item.setEvent(proxy);
		    suggestions.add(item);
		}
	    }
			
			
		} catch (Exception e) {
			throw org.psystems.dicom.browser.server.Util.throwPortalException("Suggestions error! ",e);
		}

		// Now set the suggestions in the response
		resp.setSuggestions(suggestions);

		// Send the response back to the client
		return resp;
	}

}