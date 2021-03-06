/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.technical.loggin.ILogger;
import org.apache.logging.log4j.Level;
import org.junit.Test;

/**
 *
 * @author Fabian Salzgeber
 * @date 31.3.2015
 * @version 1.0
 *
 */

public class Logger4JTest implements ILogger {

    @Test
    public void testLogging() throws Exception{
        // common logging levels and pattern showcase
        log.trace("trace message test");
        log.debug("debug message test");
        log.info("info message test");
        log.warn("warn message test");
        log.error("error message test");
        log.fatal("fatal message test");

        // advanced log
        log.log(Level.WARN,"switch levels during runtime");
    }
}
