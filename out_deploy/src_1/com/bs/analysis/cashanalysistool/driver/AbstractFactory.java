package com.bs.analysis.cashanalysistool.driver;

import com.bs.analysis.cashanalysistool.gui.validator.context.ViewScrnContext;

public abstract class AbstractFactory{
	public abstract AbstractCashAnalysisTool createCashAnalysisTool(String fileName, ViewScrnContext Context, String OutputMode, String ConfigMode, String fileOutput, String formatted, String dateformatted);
}
