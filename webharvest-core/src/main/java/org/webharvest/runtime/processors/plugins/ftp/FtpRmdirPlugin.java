package org.webharvest.runtime.processors.plugins.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.webharvest.runtime.DynamicScopeContext;
import org.webharvest.runtime.processors.WebHarvestPlugin;
import org.webharvest.runtime.variables.EmptyVariable;
import org.webharvest.runtime.variables.Variable;
import org.webharvest.utils.CommonUtil;

/**
 * Ftp Mkdir plugin - can be used only inside ftp plugin for removing subdirectory on remote directory.
 */
public class FtpRmdirPlugin extends WebHarvestPlugin {

    public String getName() {
        return "ftp-rmdir";
    }

    public Variable executePlugin(DynamicScopeContext context) {
        FtpPlugin ftpPlugin = (FtpPlugin) getParentProcessor();
        if (ftpPlugin != null) {
            FTPClient ftpClient = ftpPlugin.getFtpClient();

            String path = CommonUtil.nvl( evaluateAttribute("path", context), "" );

            setProperty("Path", path);

            try {
                boolean succ = ftpClient.removeDirectory(path);
                if (!succ) {
                    throw new FtpPluginException("Cannot remove directory \"" + path + "\" on FTP server!");
                }
            } catch (IOException e) {
                throw new FtpPluginException(e);
            }
        } else {
            throw new FtpPluginException("Cannot use ftp rmdir plugin out of ftp plugin context!");
        }

        return EmptyVariable.INSTANCE;
    }

    public String[] getValidAttributes() {
        return new String[] {"path"};
    }

    public String[] getRequiredAttributes() {
        return new String[] {"path"};
    }

    public boolean hasBody() {
        return false;
    }

}