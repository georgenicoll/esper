/**************************************************************************************
 * Copyright (C) 2006-2015 EsperTech Inc. All rights reserved.                        *
 * http://www.espertech.com/esper                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.named;

import com.espertech.esper.client.EventType;
import com.espertech.esper.core.context.util.EPStatementAgentInstanceHandle;
import com.espertech.esper.core.service.StatementAgentInstanceLock;
import com.espertech.esper.core.service.StatementResultService;
import com.espertech.esper.core.service.resource.StatementResourceService;
import com.espertech.esper.epl.lookup.IndexMultiKey;
import com.espertech.esper.epl.metric.StatementMetricHandle;
import com.espertech.esper.event.vaevent.ValueAddEventProcessor;
import com.espertech.esper.view.ViewProcessingException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service to manage named window dispatches, locks and processors on an engine level.
 */
public interface NamedWindowService
{
    /**
     * Error message for data windows required.
     */
    public final static String ERROR_MSG_DATAWINDOWS = "Named windows require one or more child views that are data window views";

    /**
     * Error message for no data window allowed.
     */
    public final static String ERROR_MSG_NO_DATAWINDOW_ALLOWED = "Consuming statements to a named window cannot declare a data window view onto the named window";

    /**
     * Returns true to indicate that the name is a named window.
     * @param name is the window name
     * @return true if a named window, false if not a named window
     */
    public boolean isNamedWindow(String name);

    /**
     * Returns the names of all named windows known.
     * @return named window names
     */
    public String[] getNamedWindows();

    /**
     * Create a new named window.
     * @param name window name
     * @param eventType the event type of the window
     * @param statementResultService for coordinating on whether insert and remove stream events should be posted
     * @param revisionProcessor handles update events
     * @param eplExpression is the expression
     * @param statementName the name of the statement
     * @param isPrioritized if the engine is running with prioritized execution
     * @return processor for the named window
     * @throws ViewProcessingException if the named window already exists
     */
    public NamedWindowProcessor addProcessor(String name,
                                             String contextName,
                                             EventType eventType,
                                             StatementResultService statementResultService,
                                             ValueAddEventProcessor revisionProcessor,
                                             String eplExpression,
                                             String statementName,
                                             boolean isPrioritized,
                                             boolean isEnableSubqueryIndexShare,
                                             boolean isBatchingDataWindow,
                                             boolean isVirtualDataWindow,
                                             StatementMetricHandle statementMetricHandle,
                                             Set<String> optionalUniqueKeyProps,
                                             String eventTypeAsName,
                                             StatementResourceService statementResourceService) throws ViewProcessingException;

    /**
     * Returns the processing instance for a given named window.
     * @param name window name
     * @return processor for the named window
     */
    public NamedWindowProcessor getProcessor(String name);

    /**
     * Upon destroy of the named window creation statement, the named window processor must be removed.
     * @param name is the named window name
     */
    public void removeProcessor(String name);

    /**
     * Dispatch events of the insert and remove stream of named windows to consumers, as part of the
     * main event processing or dispatch loop.
     * @return send events to consuming statements
     */
    public boolean dispatch();

    /**
     * For use to add a result of a named window that must be dispatched to consuming views.
     * @param delta is the result to dispatch
     * @param consumers is the destination of the dispatch, a map of statements to one or more consuming views
     */
    public void addDispatch(NamedWindowDeltaData delta, Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> consumers);

    /**
     * Returns the statement lock for the named window, to be shared with on-delete statements for the same named window.
     * @param windowName is the window name
     * @return the lock for the named window, or null if the window dos not yet exists
     */
    public StatementAgentInstanceLock getNamedWindowLock(String windowName);

    /**
     * Sets the lock to use for a named window.
     * @param windowName is the named window name
     * @param statementResourceLock is the statement lock for the create window statement
     * @param statementName the name of the statement that is the "create window"
     */
    public void addNamedWindowLock(String windowName, StatementAgentInstanceLock statementResourceLock, String statementName);

    /**
     * Remove the lock associated to the named window.
     * @param statementName the name of the statement that is the "create window"
     */
    public void removeNamedWindowLock(String statementName);

    /**
     * Clear out the service.
     */
    public void destroy();

    /**
     * Add an observer to be called back when named window state changes occur.
     * <p>
     * Observers have set-semantics: the same Observer cannot be added twice
     * @param observer to add
     */
    public void addObserver(NamedWindowLifecycleObserver observer);

    /**
     * Remove an observer to be called back when named window state changes occur.
     * @param observer to remove
     */
    public void removeObserver(NamedWindowLifecycleObserver observer);

    /**
     * Returns an index descriptor array describing all available indexes for the named window.
     * @param windowName window name
     * @return indexes
     */
    public IndexMultiKey[] getNamedWindowIndexes(String windowName);
}
