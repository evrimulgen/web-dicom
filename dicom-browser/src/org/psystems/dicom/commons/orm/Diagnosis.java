package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Диагноз
 * 
 * @author dima_d
 */
public class Diagnosis implements Serializable {

	private static final long serialVersionUID = -2133521701334810608L;
	public static final String TYPE_MAIN = "ОСНОВНОЙ";
	public static final String TYPE_INVOLVEMENT = "ОСЛОЖНЕНИЕ";
	public static final String TYPE_ACCOMPANYING = "СОПУТСТВУЮЩИЙ";
	static String persistentDelimeter = "|";// разделитель структур

	// TODO Доделать типы...
	// Предварительный, Уточненный, Выписки, Направления, Приемного отделения,
	// Клинический, Смерти, Паталогоанатомический.
	public static final String SUBTYPE_MAIN = "";

	private String diagnosisType;// Тип
	private String diagnosisSubType;// ПодтипТип
	private String diagnosisCode;// Код (по МКБ)
	private String diagnosisDescription;// Описание

	
	
	/**
	 * формат строки диагнозов: Тип^подтип^МКБ^Описание|...;
	 * 
	 * @return
	 */
	public String toPersistentString() {

		return ORMUtil.toPersistString(diagnosisType) + "^"
				+ ORMUtil.toPersistString(diagnosisSubType) + "^"
				+ ORMUtil.toPersistString(diagnosisCode) + "^"
				+ ORMUtil.toPersistString(diagnosisDescription);

	}

	/**
	 * Создание экземпляра из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Diagnosis getFromPersistentString(String data) {

		if (data == null || data.length() == 0)
			return null;

		Diagnosis dia = new Diagnosis();

		Matcher matcher = Pattern.compile("^(.+)\\^(.+)\\^(.+)\\^(.+)$")
				.matcher(data);
		if (matcher.matches()) {
			dia.setDiagnosisType(ORMUtil.fromPersistString(matcher.group(1)));
			dia.setDiagnosisSubType(ORMUtil.fromPersistString(matcher.group(2)));
			dia.setDiagnosisCode(ORMUtil.fromPersistString(matcher.group(3)));
			dia.setDiagnosisDescription(ORMUtil.fromPersistString(matcher.group(4)));

		} else {
			throw new IllegalArgumentException(
					"Wrong string pattern [diagnosisType^diagnosisSubType^diagnosisCode^diagnosisDescription] for ["
							+ data + "]");
		}

		return dia;
	}

	/**
	 * Создание коллекции экземпляров из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Diagnosis[] getCollectionFromPersistentString(
			String data) {
		if(data==null) return null;
		
		ArrayList<Diagnosis> dias = new ArrayList<Diagnosis>();
		String[] d = data.split("\\" + persistentDelimeter);
		for (int i = 0; i < d.length; i++) {
			dias.add(getFromPersistentString(d[i]));
		}
		Diagnosis[] result = new Diagnosis[dias.size()];
		return dias.toArray(result);
	}

	/**
	 * Получение коллекции в строковом варианте для сохранения
	 * 
	 * @param dialist
	 * @return
	 */
	public static String toPersistentCollectionString(
			Diagnosis[] dialist) {
		StringBuffer sb = new StringBuffer();
		for (Diagnosis diagnosis : dialist) {
			if (sb.length() != 0)
				sb.append(persistentDelimeter);
			sb.append(diagnosis.toPersistentString());
		}
		return sb.toString();
	}

	public String getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public String getDiagnosisSubType() {
		return diagnosisSubType;
	}

	public void setDiagnosisSubType(String diagnosisSubType) {
		this.diagnosisSubType = diagnosisSubType;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}

	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
	}

	@Override
	public String toString() {
		return "Diagnosis [diagnosisCode=" + diagnosisCode
				+ ", diagnosisDescription=" + diagnosisDescription
				+ ", diagnosisSubType=" + diagnosisSubType + ", diagnosisType="
				+ diagnosisType + "]";
	}

}
