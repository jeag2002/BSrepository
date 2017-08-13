package com.bs.analysis.cashanalysistool.ae;


public class ProcessSignals {
	
	private String finishSignal = null;
	private static ProcessSignals INSTANCE;
	
    private ProcessSignals(){
    	finishSignal = "NO";
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new ProcessSignals();
        }
    }
    
    public static ProcessSignals getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

	public String getFinishSignal() {
		return finishSignal;
	}

	public void setFinishSignal(String finishSignal) {
		this.finishSignal = finishSignal;
	}
}
