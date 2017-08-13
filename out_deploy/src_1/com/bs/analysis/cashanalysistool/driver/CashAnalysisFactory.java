package com.bs.analysis.cashanalysistool.driver;

import com.bs.analysis.cashanalysistool.gui.validator.context.ViewScrnContext;

public class CashAnalysisFactory extends AbstractFactory{
	public AbstractCashAnalysisTool createCashAnalysisTool(String FileName, ViewScrnContext Context, String OutputMode, String ConfigMode, String fileOutput, String formatted, String dateformatted)
	{return new CashAnalysisTool(FileName,Context,OutputMode, ConfigMode, fileOutput, formatted, dateformatted);}
}
