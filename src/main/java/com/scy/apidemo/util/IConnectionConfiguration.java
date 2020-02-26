package com.scy.apidemo.util;


/** Delegate for connection parameters */
public interface IConnectionConfiguration {

	public String getDefaultHost();
	public String getDefaultPort();
	public String getDefaultConnectOptions();

	/** Standard ApiDemo configuration for pre-v100 connection */
	public static class DefaultConnectionConfiguration implements IConnectionConfiguration {
	    @Override public String getDefaultHost() { return ""; }
	    @Override public String getDefaultPort() { return ""; }
	    @Override public String getDefaultConnectOptions() { return null; }
	}
}
