package com.spj.salon.configs;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfig {
	
    @Autowired
    private Environment environment;

    private String port;
    private String hostname;

    /**
     * Get port.
     *
     * @return
     */
    public String getPort() {
        if (port == null) port = System.getenv("local.server.port");
        return port;
    }

    /**
     * Get port, as Integer.
     *
     * @return
     */
    public Integer getPortAsInt() {
        return Integer.valueOf(getPort());
    }

    /**
     * Get hostname.
     *
     * @return
     */
    public String getHostname() throws UnknownHostException {
        // TODO ... would this cache cause issue, when network env change ???
        if (hostname == null) hostname = InetAddress.getLocalHost().getHostAddress();
        return hostname;
    }

    public String getServerUrlPrefi() throws UnknownHostException {
        return "http://" + getHostname() + ":" + getPort();
    }

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @return the twilioOtpSid
	 */
	public String getTwilioOtpSid() {
		return System.getenv("twiliootp-sid");
	}

	/**
	 * @return the twilioOtpAuthToken
	 */
	public String getTwilioOtpAuthToken() {
		return System.getenv("twiliootp-authtoken");
	}

	/**
	 * @return the jwtSigningKey
	 */
	public String getJwtSigningKey() {
		return System.getenv("jwt-signingkey");
	}

	/**
	 * @return the googleApiKey
	 */
	public String getGoogleApiKey() {
		return System.getenv("googleapikey");
	}

	/**
	 * @return the mailUsername
	 */
	public String getMailUsername() {
		return System.getenv("mail-username");
	}

	/**
	 * @return the mailPassword
	 */
	public String getMailPassword() {
		return System.getenv("mail-password");
	}

	/**
	 * @return the datasourceUsername
	 */
	public String getDatasourceUsername() {
		return System.getenv("datasource-username");
	}

	/**
	 * @return the datasourcePassword
	 */
	public String getDatasourcePassword() {
		return System.getenv("datasource-password");
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQuotoGuardShieldURL() {
		return System.getenv("QUOTAGUARDSHIELD_URL");
	}
}
