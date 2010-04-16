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
    
    Russian translation <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
     
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
 */
package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dima_d
 * 
 */
public class StudyImpl extends Study {

	public static Study getInstance(long id) {
		StudyImpl stub = new StudyImpl();
		stub.setId(id);
		return stub;
	}

	public static List<Study> getStudues(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Study[] getStudues(Connection connection, String studyType,
			String patientName, Date patientBirthDate, String patientSex,
			Date beginStudyDate, Date endStudyDate) throws DataException {

		PreparedStatement psSelect = null;

		// "SELECT * FROM WEBDICOM.STUDY"
		// + " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
		// + " order by PATIENT_NAME, STUDY_DATE "

		String sqlAddon = "";

		if (studyType != null) {
		}// TODO Сделать обработку studyType !!!

		if (patientName != null) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " UPPER(PATIENT_NAME) like UPPER( ? || '%') ";
		}

		if (patientBirthDate != null) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_BIRTH_DATE = ? ";
		}

		if (patientSex != null) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_SEX = ? ";
		}

		if (beginStudyDate != null && endStudyDate != null) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_DATE BETWEEN ? AND ? ";
		}

		String sql = "SELECT * FROM WEBDICOM.STUDY" + " WHERE " + sqlAddon
				+ " order by PATIENT_NAME, STUDY_DATE ";
		
//		System.err.println("SQL:"+sql);

		try {

			psSelect = connection.prepareStatement(sql);
			int index = 1;

			if (studyType != null) {
			}// TODO Сделать обработку studyType !!!

			if (patientName != null) {
				psSelect.setString(index++, patientName);	
			}

			if (patientBirthDate != null) {
				psSelect.setDate(index++, new java.sql.Date(patientBirthDate.getTime()));
			}

			if (patientSex != null) {
				psSelect.setString(index++, patientSex);
			}

			if (beginStudyDate != null && endStudyDate != null) {
				psSelect.setDate(index++, new java.sql.Date(beginStudyDate.getTime()));
				psSelect.setDate(index++, new java.sql.Date(endStudyDate.getTime()));
			}
			
			
			ResultSet rs = psSelect.executeQuery();

			ArrayList<Study> data = new ArrayList<Study>();

			while (rs.next()) {

				Study study = new StudyImpl();
				study.setId(rs.getLong("ID"));
				study.setStudyType(rs.getString("STUDY_TYPE"));
				study.setStudyDate(rs.getDate("STUDY_DATE"));
//				study.setManufacturerModelUID(rs
//						.getString("STUDY_MANUFACTURER_UID"));
				study.setManufacturerModelUID("");//TODO сделать!!
				study.setManufacturerModelName(rs
						.getString("STUDY_MANUFACTURER_MODEL_NAME"));
				study.setStudyDoctor(rs.getString("STUDY_DOCTOR"));
				study.setStudyId(rs.getString("STUDY_ID"));
				study.setPatientName(rs.getString("PATIENT_NAME"));
				study.setPatientSex(rs.getString("PATIENT_SEX"));
				study.setPatientBirthDate(rs.getDate("PATIENT_BIRTH_DATE"));
				study.setPatientId(rs.getString("PATIENT_ID"));
				study.setStudyResult(rs.getString("STUDY_RESULT"));
				study.setStudyUrl("");// TODO сделать!!
				study.setDcmFiles(new Long[] { 1l, 2l, 3l });// TODO сделать!!
				data.add(study);

			}
			rs.close();

			Study[] result = new Study[data.size()];
			return data.toArray(result);

		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}

	}

}
