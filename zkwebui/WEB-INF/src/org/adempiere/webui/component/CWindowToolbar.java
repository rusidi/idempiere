/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.event.ToolbarListener;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Feb 25, 2007
 * @version $Revision: 0.10 $
 */
public class CWindowToolbar extends FToolbar implements EventListener
{
    private static final long serialVersionUID = 1L;
    
    private static final String BUTTON_WIDTH = "32px";
    
    private static CLogger log = CLogger.getCLogger(CWindowToolbar.class);

    private ToolBarButton btnIgnore;

    private ToolBarButton btnHelp, btnNew, btnDelete, btnSave;

    private ToolBarButton btnRefresh, btnFind, btnAttachment;
    
    private ToolBarButton btnGridToggle;

    private ToolBarButton btnHistoryRecords, btnMenu, btnParentRecord, btnDetailRecord;

    private ToolBarButton btnFirst, btnPrevious, btnNext, btnLast;

    private ToolBarButton btnReport, btnArchive, btnPrint;

    private ToolBarButton btnZoomAcross, btnActiveWorkflows, btnRequests, btnProductInfo;

//    private ToolBarButton btnExit;
    
    private ArrayList<ToolbarListener> listeners = new ArrayList<ToolbarListener>();

private Event event;

    public CWindowToolbar()
    {
        init();
    }

    private void init()
    {
    	LayoutUtils.addSclass("adwindow-toolbar", this);
    	
        btnIgnore = new ToolBarButton();
        btnIgnore.setName("btnIgnore");
        btnIgnore.setImage("/images/Ignore16.gif");
        btnIgnore.setTooltiptext(Msg.getMsg(Env.getCtx(),"Ignore"));
        // --
        btnHelp = new ToolBarButton("");
        btnHelp.setName("btnHelp");
        btnHelp.setImage("/images/Help24.gif");
        btnHelp.setTooltiptext(Msg.getMsg(Env.getCtx(),"Help"));

        btnNew = new ToolBarButton("");
        btnNew.setName("btnNew");
        btnNew.setImage("/images/New24.gif");
        btnNew.setTooltiptext(Msg.getMsg(Env.getCtx(),"New"));
        
        btnDelete = new ToolBarButton("");
        btnDelete.setName("btnDelete");
        btnDelete.setImage("/images/Delete24.gif");
        btnDelete.setTooltiptext(Msg.getMsg(Env.getCtx(),"Delete"));

        btnSave = new ToolBarButton("");
        btnSave.setName("btnSave");
        btnSave.setImage("/images/Save24.gif");
        btnSave.setTooltiptext(Msg.getMsg(Env.getCtx(),"Save"));
        // --
        btnRefresh = new ToolBarButton("");
        btnRefresh.setName("btnRefresh");
        btnRefresh.setImage("/images/Refresh24.gif");
        btnRefresh.setTooltiptext(Msg.getMsg(Env.getCtx(),"Refresh"));

        btnFind = new ToolBarButton("");
        btnFind.setName("btnFind");
        btnFind.setImage("/images/Find24.gif");
        btnFind.setTooltiptext(Msg.getMsg(Env.getCtx(),"Find"));

        btnAttachment = new ToolBarButton("");
        btnAttachment.setName("btnAttachment");
        btnAttachment.setImage("/images/Attachment24.gif");
        btnAttachment.setTooltiptext(Msg.getMsg(Env.getCtx(),"Attachment"));
        // --
        
        btnGridToggle = new ToolBarButton("");
        btnGridToggle.setName("btnGridToggle");
        btnGridToggle.setImage("/images/Multi24.gif");
        btnGridToggle.setTooltiptext(Msg.getMsg(Env.getCtx(),"Multi"));
        
        btnHistoryRecords = new ToolBarButton("");
        btnHistoryRecords.setName("btnHistoryRecords");
        btnHistoryRecords.setImage("/images/HistoryX24.gif");
        btnHistoryRecords.setTooltiptext(Msg.getMsg(Env.getCtx(),"History"));

        btnMenu = new ToolBarButton("");
        btnMenu.setName("btnHome");
        btnMenu.setImage("/images/Home24.gif");
        btnMenu.setTooltiptext(Msg.getMsg(Env.getCtx(),"Home"));

        btnParentRecord = new ToolBarButton("");
        btnParentRecord.setName("btnParentRecord");
        btnParentRecord.setImage("/images/Parent24.gif");
        btnParentRecord.setTooltip(Msg.getMsg(Env.getCtx(),"Parent"));

        btnDetailRecord = new ToolBarButton("");
        btnDetailRecord.setName("btnDetailRecord");
        btnDetailRecord.setImage("/images/Detail24.gif");
        btnDetailRecord.setTooltip(Msg.getMsg(Env.getCtx(),"Detail"));
        // --
        btnFirst = new ToolBarButton("");
        btnFirst.setName("btnFirst");
        btnFirst.setImage("/images/First24.gif");
        btnFirst.setTooltip(Msg.getMsg(Env.getCtx(),"First"));

        btnPrevious = new ToolBarButton("");
        btnPrevious.setName("btnPrevious");
        btnPrevious.setImage("/images/Previous24.gif");
        btnPrevious.setTooltip(Msg.getMsg(Env.getCtx(),"Previous"));

        btnNext = new ToolBarButton("");
        btnNext.setName("btnNext");
        btnNext.setImage("/images/Next24.gif");
        btnNext.setTooltip(Msg.getMsg(Env.getCtx(),"Next"));

        btnLast = new ToolBarButton("");
        btnLast.setName("btnLast");
        btnLast.setImage("/images/Last24.gif");
        btnLast.setTooltip(Msg.getMsg(Env.getCtx(),"Last"));
        
        // --
        btnReport = new ToolBarButton("");
        btnReport.setName("btnReport");
        btnReport.setImage("/images/Report24.gif");
        btnReport.setTooltip(Msg.getMsg(Env.getCtx(),"Report"));

        btnArchive = new ToolBarButton("");
        btnArchive.setName("btnArchive");
        btnArchive.setImage("/images/Archive24.gif");
        btnArchive.setTooltip(Msg.getMsg(Env.getCtx(),"Archive"));

        btnPrint = new ToolBarButton("");
        btnPrint.setName("btnPrint");
        btnPrint.setImage("/images/Print24.gif");
        btnPrint.setTooltip(Msg.getMsg(Env.getCtx(),"Print"));
        
        // --
        btnZoomAcross = new ToolBarButton("");
        btnZoomAcross.setName("btnZoomAcross");
        btnZoomAcross.setImage("/images/ZoomAcross24.gif");
        btnZoomAcross.setTooltip(Msg.getMsg(Env.getCtx(),"ZoomAcross"));

        btnActiveWorkflows = new ToolBarButton("");
        btnActiveWorkflows.setName("btnActiveWorkflows");
        btnActiveWorkflows.setImage("/images/WorkFlow24.gif");
        btnActiveWorkflows.setTooltip(Msg.getMsg(Env.getCtx(),"WorkFlow"));

        btnRequests = new ToolBarButton("");
        btnRequests.setName("btnRequests");
        btnRequests.setImage("/images/Request24.gif");
        btnRequests.setTooltip(Msg.getMsg(Env.getCtx(),"Request"));

        btnProductInfo = new ToolBarButton("");
        btnProductInfo.setName("btnProductInfo");
        btnProductInfo.setImage("/images/Product24.gif");
        btnProductInfo.setTooltip(Msg.getMsg(Env.getCtx(),"InfoProduct"));

//        btnExit = new ToolBarButton("");
//        btnExit.setName("btnExit");
//        btnExit.setImage("/images/End24.gif");

        this.appendChild(btnIgnore);
        addSeparator();
        this.appendChild(btnHelp);
        this.appendChild(btnNew);
//        this.appendChild(btnEdit);
        this.appendChild(btnDelete);
        this.appendChild(btnSave);
        addSeparator();
        this.appendChild(btnRefresh);
        this.appendChild(btnFind);
        this.appendChild(btnAttachment);
        this.appendChild(btnGridToggle);
        addSeparator();
        this.appendChild(btnHistoryRecords);
        this.appendChild(btnMenu);
        this.appendChild(btnParentRecord);
        this.appendChild(btnDetailRecord);
        addSeparator();
        this.appendChild(btnFirst);
        this.appendChild(btnPrevious);
        this.appendChild(btnNext);
        this.appendChild(btnLast);
        addSeparator();
        this.appendChild(btnReport);
        this.appendChild(btnArchive);
        this.appendChild(btnPrint);
        addSeparator();
        this.appendChild(btnZoomAcross);
        this.appendChild(btnActiveWorkflows);
        this.appendChild(btnRequests);
        this.appendChild(btnProductInfo);
//        addSeparator();
//        this.appendChild(btnExit);
        
        for (Object obj : this.getChildren())
        {
            if (obj instanceof ToolBarButton)
            {
                ((ToolBarButton)obj).setWidth(BUTTON_WIDTH);
                ((ToolBarButton)obj).addEventListener(Events.ON_CLICK, this);
                ((ToolBarButton)obj).setDisabled(true);
            }
        }
        
        // Help and Exit should always be enabled
        btnHelp.setDisabled(false);
//        btnExit.setDisabled(false);
        
        // Testing Purposes
        
        btnGridToggle.setDisabled(false);
        
        btnZoomAcross.setDisabled(false);
        
        btnActiveWorkflows.setDisabled(false); // Elaine 2008/07/17
    }

    protected void addSeparator()
    {
        Label lblSeparator = new Label();
        lblSeparator.setWidth("3px");
        lblSeparator.setHeight("16px");
        lblSeparator.setValue(" ");
        this.appendChild(lblSeparator);
    }

    public void addListener(ToolbarListener toolbarListener)
    {
        listeners.add(toolbarListener);
    }

    public void removeListener(ToolbarListener toolbarListener)
    {
        listeners.remove(toolbarListener);
    }

    public void onEvent(Event event)
    {    	
        String eventName = event.getName();
        Component eventComp = event.getTarget();

        Iterator<ToolbarListener> listenerIter = listeners.iterator();
        if(eventName.equals(Events.ON_CLICK))
        {
            if(eventComp instanceof ToolBarButton)
            {
            	this.event = event;
            	ToolBarButton cComponent = (ToolBarButton) eventComp;
                String compName = cComponent.getName();
                String methodName = "on" + compName.substring(3);
                while(listenerIter.hasNext())
                {
                    try
                    {
                        ToolbarListener tListener = listenerIter.next();
                        Method method = tListener.getClass().getMethod(methodName, (Class[]) null);
                        method.invoke(tListener, (Object[]) null);
                    }
                    catch(SecurityException e)
                    {
                        log.log(Level.SEVERE, "Could not invoke Toolbar listener method: " + methodName + "()", e);
                    }
                    catch(NoSuchMethodException e)
                    {
                        log.log(Level.SEVERE, "Could not invoke Toolbar listener method: " + methodName + "()", e);
                    }
                    catch(IllegalArgumentException e)
                    {
                        log.log(Level.SEVERE, "Could not invoke Toolbar listener method: " + methodName + "()", e);
                    }
                    catch(IllegalAccessException e)
                    {
                        log.log(Level.SEVERE, "Could not invoke Toolbar listener method: " + methodName + "()", e);
                    }
                    catch(InvocationTargetException e)
                    {
                        log.log(Level.SEVERE, "Could not invoke Toolbar listener method: " + methodName + "()", e);
                    }
                }
                this.event = null;
            }
        }
    }
    
    public void enableHistoryRecords(boolean enabled)
    {
    	this.btnHistoryRecords.setDisabled(!enabled);
    }
    
    public void enableNavigation(boolean enabled)
    {
        this.btnFirst.setDisabled(!enabled);
        this.btnPrevious.setDisabled(!enabled);
        this.btnNext.setDisabled(!enabled);
        this.btnLast.setDisabled(!enabled);
    }
    
    public void enableTabNavigation(boolean enabled)
    {
        enableTabNavigation(enabled, enabled);
    }
    
    public void enableTabNavigation(boolean enableParent, boolean enableDetail)
    {
        this.btnParentRecord.setDisabled(!enableParent);
        this.btnDetailRecord.setDisabled(!enableDetail);
    }
    
    public void enableFirstNavigation(boolean enabled)
    {
        this.btnFirst.setDisabled(!enabled);
        this.btnPrevious.setDisabled(!enabled);
    }
    
    public void enableLastNavigation(boolean enabled)
    {
        this.btnLast.setDisabled(!enabled);
        this.btnNext.setDisabled(!enabled);
    }
    
    public void enabledNew(boolean enabled)
    {
        this.btnNew.setDisabled(!enabled);
    }
    
   /* public void enableEdit(boolean enabled)
    {
        this.btnEdit.setEnabled(enabled);
    }*/
    
    public void enableRefresh(boolean enabled)
    {
        this.btnRefresh.setDisabled(!enabled);
    }
    
    public void enableSave(boolean enabled)
    {
        this.btnSave.setDisabled(!enabled);
    }
    
//    public void enableExit(boolean enabled)
//    {
//    	this.btnExit.setDisabled(!enabled);
//    }
    
    public void enableDelete(boolean enabled)
    {
        this.btnDelete.setDisabled(!enabled);
    }
    
    public void enableDeleteSelection(boolean enabled)
    {
        // TODO add delete selection button
    }
    
    public void enableChanges(boolean enabled)
    {
        this.btnNew.setDisabled(!enabled);
        this.btnIgnore.setDisabled(!enabled);
    }
    
    public void enableIgnore(boolean enabled)
    {
        this.btnIgnore.setDisabled(!enabled);
    }
    
    public void enableNew(boolean enabled)
    {
        this.btnNew.setDisabled(!enabled);
    }
    
    public void enableAttachment(boolean enabled)
    {
        this.btnAttachment.setDisabled(!enabled);
    }
    
    public void enablePrint(boolean enabled)
    {
    	this.btnPrint.setDisabled(!enabled);
    }
    
    public void enableReport(boolean enabled)
    {
    	this.btnReport.setDisabled(!enabled);
    }
    
    public void enableCopy(boolean enabled)
    {
    }
    
    public void enableFind(boolean enabled)
    {
        this.btnFind.setDisabled(!enabled);
        
    }
    
    public void enableGridToggle(boolean enabled)
    {
    	btnGridToggle.setDisabled(!enabled);
    }
    
    public boolean isAsap()
    {
        return true;
    }    
    
    public Event getEvent() 
    {
    	return event;
    }
}