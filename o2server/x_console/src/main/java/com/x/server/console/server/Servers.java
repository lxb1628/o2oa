package com.x.server.console.server;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.ftpserver.FtpServer;
import org.eclipse.jetty.server.Server;

import com.x.base.core.project.config.Config;
import com.x.base.core.project.config.DataServer;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.server.console.server.admin.AdminServerTools;
import com.x.server.console.server.application.ApplicationServerTools;
import com.x.server.console.server.center.CenterServerTools;
import com.x.server.console.server.data.DataServerTools;
import com.x.server.console.server.data.DataTcpWebServer;
import com.x.server.console.server.storage.StorageServerTools;
import com.x.server.console.server.web.WebServerTools;

public class Servers {

	private static final Logger LOGGER = LoggerFactory.getLogger(Servers.class);

	private Servers() {

	}

	private static Server adminServer;
	private static Server centerServer;
	private static Server webServer;
	private static Server applicationServer;
	private static FtpServer storageServer;
	private static DataTcpWebServer dataServer;

	public static Server getAdminServer() {
		return adminServer;
	}

	public static Server getCenterServer() {
		return centerServer;
	}

	public static Server getApplicationServer() {
		return applicationServer;
	}

	public static Server getWebServer() {
		return webServer;
	}

	public static boolean adminServerIsStarted() {
		if (null == adminServer) {
			return false;
		}
		return adminServer.isStarted();
	}

	public static boolean webServerIsStarted() {
		if (null == webServer) {
			return false;
		}
		return webServer.isStarted();
	}

	public static boolean applicationServerIsStarted() {
		if (null == applicationServer) {
			return false;
		}
		return applicationServer.isStarted();
	}

	public static boolean centerServerIsStarted() {
		if (null == centerServer) {
			return false;
		}
		return centerServer.isStarted();
	}

	public static boolean adminServerIsRunning() {
		if (null == adminServer) {
			return false;
		}
		return adminServer.isRunning();
	}

	public static boolean webServerIsRunning() {
		if (null == webServer) {
			return false;
		}
		return webServer.isRunning();
	}

	public static boolean applicationServerIsRunning() {
		if (null == applicationServer) {
			return false;
		}
		return applicationServer.isRunning();
	}

	public static boolean centerServerIsRunning() {
		if (null == centerServer) {
			return false;
		}
		return centerServer.isRunning();
	}

	public static boolean storageServerIsRunning() {
		if (null == storageServer) {
			return false;
		}
		return (!storageServer.isStopped());
	}

	public static boolean dataServerIsRunning() {
		if (null == dataServer) {
			return false;
		}
		return (dataServer.isRunning());
	}

	public static void startAdminServer() throws Exception {
		if (adminServerIsRunning()) {
			LOGGER.info("admin server is running.");
		} else {
			adminServer = AdminServerTools.start();
		}
	}

	public static void startWebServer() throws Exception {
		if (webServerIsRunning()) {
			LOGGER.info("web server is running.");
		} else {
			webServer = WebServerTools.start();
		}
	}

	public static void startApplicationServer() throws Exception {
		if (applicationServerIsRunning()) {
			LOGGER.info("application server is running.");
		} else {
			applicationServer = ApplicationServerTools.start();
		}
	}

	public static void startCenterServer() throws Exception {
		if (centerServerIsRunning()) {
			LOGGER.info("center server is running.");
		} else {
			centerServer = CenterServerTools.start();
		}

	}

	public static void startStorageServer() throws Exception {
		if (storageServerIsRunning()) {
			LOGGER.info("storage server is running.");
		} else {
			storageServer = StorageServerTools.start();
		}
	}

	public static void startDataServer() throws Exception {
		if (dataServerIsRunning()) {
			LOGGER.info("data server is running.");
		} else {
			DataServer server = Config.currentNode().getData();
			if (null == server) {
				LOGGER.info("data server is not configured.");
			} else if (!BooleanUtils.isTrue(server.getEnable())) {
				LOGGER.info("data server is not enable.");
			} else {
				dataServer = DataServerTools.start();
			}
		}
	}

	public static void stopAdminServer() throws Exception {
		if (adminServerIsRunning()) {
			adminServer.stop();
		}
	}

	public static void stopWebServer() throws Exception {
		if (webServerIsRunning()) {
			webServer.stop();
		}
	}

	public static void stopApplicationServer() throws Exception {
		if (applicationServerIsRunning()) {
			applicationServer.stop();
		}
	}

	public static void stopCenterServer() throws Exception {
		if (centerServerIsRunning()) {
			centerServer.stop();
		}
	}

	public static void stopStorageServer() {
		if (storageServerIsRunning()) {
			storageServer.stop();
		}
	}

	public static void stopDataServer() {
		if (dataServerIsRunning()) {
			dataServer.stop();
		}
	}

}
