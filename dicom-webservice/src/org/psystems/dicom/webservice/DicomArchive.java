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
package org.psystems.dicom.webservice;

import java.util.ArrayList;
import java.util.Date;

public class DicomArchive {

	public org.psystems.dicom.webservice.Study getStudy(int i) {

		Study study = new Study();
		study.setId(i);
		study.setStudyDate(new Date());
		study.setStudyDescription("исследование № " + i);
		study.setStudyDevice("Аппарат №1");
		study.setStudyDoctor("Врач №1");
		study.setStudyId("studyID=" + i);
		study.setStudyPatient("Пациент №1");
		study.setStudyPatientId("patientID=ХХХ");
		study.setStudyResult("Результат 'норма'");
		study.setStudyType("флюорография");
		study.setStudyUrl("http://localhost/" + i + ".dcm");

		return study;
	}

	public Study[] findStudies(String s) {

		ArrayList<Study> data = new ArrayList<Study>();
		for (int i = 0; i < 10; i++) {
			Study study = new Study();
			study.setId(i);
			study.setStudyDate(new Date());
			study.setStudyDescription("исследование № " + i);
			study.setStudyDevice("Аппарат №1");
			study.setStudyDoctor("Врач №1");
			study.setStudyId("studyID=" + i);
			study.setStudyPatient("Пациент №1");
			study.setStudyPatientId("patientID=ХХХ");
			study.setStudyResult("Результат 'норма'");
			study.setStudyType("флюорография");
			study.setStudyUrl("http://localhost/" + i + ".dcm");
			data.add(study);
		}

		Study[] result = new Study[data.size()];
		return data.toArray(result);
	}

	/**
	 * Создание нового исследования
	 * 
	 * @param PatientId
	 * @param Patient
	 * @param patientDateBirthday
	 * @param patientSex
	 * @param studyType
	 * @return
	 */
	public int newStudy(String PatientId, String Patient,
			Date patientDateBirthday, String patientSex, String studyType) {
		//
		// private int id;// Идентификатор (внутренний)
		// private String studyDescription; // Описание
		// private String studyResult; // Результат исследования
		// private String studyId;// Идентификатор исследования
		// private String studyUrl; // URL для открытия в обозревателе
		// // private String studyPatient;// ФИО пациента
		// // private String studyPatientId;// Идентификатор пациента
		// private String studyDoctor; // Врач
		// private Date studyDate;// Дата исследования
		// private String studyType;// Вид исследования
		// private String studyDevice;// Аппарат, на котором проводилось
		// // исследование.

		return 1;
	}

}