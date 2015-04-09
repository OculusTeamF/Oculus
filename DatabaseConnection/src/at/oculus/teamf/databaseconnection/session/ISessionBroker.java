/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

/**
 * SessionBroker interface to abstract a broker that deal sessions that implement the {@code #ISession}
 * <p/>
 *
 * @author Simon Angerer
 * @date 30.03.2015
 * @version 1.0
 */
public interface ISessionBroker {

	/**
	 * Creates a new ISession and retuns it to the caller. Session caching is handelt internally!
	 * A ISession can be released with {@code #releaseSession()}
	 *
	 * @return a new {@code #ISession}
	 */
	ISession getSession();

	/**
	 * Releases the Session back to the broker. Note after relessing the session it can be closed or dealt to an other
	 * object. So it should not be use again use {@code #getSession()} to request a new Session.
	 *
	 * @param session
	 * 		a {@code #ISession} that is no longer needed.
	 */
	void releaseSession(ISession session);
}
