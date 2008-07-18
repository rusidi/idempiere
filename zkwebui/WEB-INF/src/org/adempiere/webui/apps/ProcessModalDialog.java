/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2007 Adempiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *
 * Copyright (C) 2007 Low Heng Sin hengsin@avantz.com
 * _____________________________________________
 *****************************************************************************/
package org.adempiere.webui.apps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.VerticalBox;
import org.adempiere.webui.component.Window;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.ProcessDialog;
import org.compiere.process.ProcessInfo;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;

/**
 * 
 *	Modal Dialog to Start process.
 *	Displays information about the process
 *		and lets the user decide to start it
 *  	and displays results (optionally print them).
 *  Calls ProcessCtl to execute.
 *  @author 	Low Heng Sin
 *  @author     arboleda - globalqss
 *  - Implement ShowHelp option on processes and reports
 */
public class ProcessModalDialog extends Window implements EventListener
{
	
	private boolean m_autoStart;


	/**
	 * Dialog to start a process/report
	 * @param ctx
	 * @param parent
	 * @param title
	 * @param aProcess
	 * @param WindowNo
	 * @param AD_Process_ID
	 * @param tableId
	 * @param recordId
	 * @param autoStart
	 */
	public ProcessModalDialog (Window parent, String title, 
			ASyncProcess aProcess, int WindowNo, int AD_Process_ID,
			int tableId, int recordId, boolean autoStart)
	{
		
		log.info("Process=" + AD_Process_ID );
		m_ctx = Env.getCtx();;
		m_ASyncProcess = aProcess;
		m_WindowNo = WindowNo;
		m_AD_Process_ID = AD_Process_ID;
		m_tableId = tableId;
		m_recordId = recordId;
		m_autoStart = autoStart;
		try
		{
			initComponents();
			init();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
	}	//	ProcessDialog

	private void initComponents() {
		VerticalBox vbox = new VerticalBox();
		Div div = new Div();
		message = new Html();
		div.appendChild(message);
		vbox.appendChild(message);
		centerPanel = new Panel();
		vbox.appendChild(centerPanel);
		div = new Div();
		div.setAlign("right");
		Hbox hbox = new Hbox();
		Button btn = new Button("Ok");
		btn.setName("ok");
		btn.addEventListener(Events.ON_CLICK, this);
		hbox.appendChild(btn);
		
		btn = new Button("Cancel");
		btn.setName("cancel");
		btn.addEventListener(Events.ON_CLICK, this);
		
		hbox.appendChild(btn);
		div.appendChild(hbox);
		vbox.appendChild(div);		
		this.appendChild(vbox);
	}

	private ASyncProcess m_ASyncProcess;
	private int m_WindowNo;
	private Properties m_ctx;
	private int m_tableId;
	private int m_recordId;
	private int 		    m_AD_Process_ID;
	private String		    m_Name = null;
	private StringBuffer	m_messageText = new StringBuffer();
	private String          m_ShowHelp = null; // Determine if a Help Process Window is shown
	private boolean m_valid = true;
	
	private Panel centerPanel = null;
	private Html message = null;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ProcessDialog.class);
	//
	private ProcessParameterPanel parameterPanel = null;
	
	private ProcessInfo m_pi = null;

	
	/**
	 * 	Set Visible 
	 * 	(set focus to OK if visible)
	 * 	@param visible true if visible
	 */
	public boolean setVisible (boolean visible)
	{
		return super.setVisible(visible);
	}	//	setVisible

	/**
	 *	Dispose
	 */
	public void dispose()
	{
		parameterPanel.restoreContext();
		m_valid = false;
		this.detach();
	}	//	dispose

	public boolean isValid()
	{
		return m_valid;
	}

	/**
	 *	Dynamic Init
	 *  @return true, if there is something to process (start from menu)
	 */
	public boolean init()
	{
		log.config("");
		//
		boolean trl = !Env.isBaseLanguage(m_ctx, "AD_Process");
		String sql = "SELECT Name, Description, Help, IsReport, ShowHelp "
				+ "FROM AD_Process "
				+ "WHERE AD_Process_ID=?";
		if (trl)
			sql = "SELECT t.Name, t.Description, t.Help, p.IsReport, p.ShowHelp "
				+ "FROM AD_Process p, AD_Process_Trl t "
				+ "WHERE p.AD_Process_ID=t.AD_Process_ID"
				+ " AND p.AD_Process_ID=? AND t.AD_Language=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_AD_Process_ID);
			if (trl)
				pstmt.setString(2, Env.getAD_Language(m_ctx));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_Name = rs.getString(1);
				m_ShowHelp = rs.getString(5);
				//
				m_messageText.append("<b>");
				String s = rs.getString(2);		//	Description
				if (rs.wasNull())
					m_messageText.append(Msg.getMsg(m_ctx, "StartProcess?"));
				else
					m_messageText.append(s);
				m_messageText.append("</b>");
				
				s = rs.getString(3);			//	Help
				if (!rs.wasNull())
					m_messageText.append("<p>").append(s).append("</p>");
			}

			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}

		if (m_Name == null)
			return false;
		//
		this.setTitle(m_Name);
		message.setContent(m_messageText.toString());
		

		//	Move from APanel.actionButton
		m_pi = new ProcessInfo(m_Name, m_AD_Process_ID, m_tableId, m_recordId);
		m_pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
		m_pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
		parameterPanel = new ProcessParameterPanel(m_WindowNo, m_pi);
		centerPanel.getChildren().clear();
		if ( parameterPanel.init() ) {
			centerPanel.appendChild(parameterPanel);
		} else {
			if (m_ShowHelp != null && m_ShowHelp.equals("N")) {
				m_autoStart = true;
			}
			if (m_autoStart) {
				startProcess();
				dispose();
			}
		}
		
		// Check if the process is a silent one
		if(isValid() && m_ShowHelp != null && m_ShowHelp.equals("S"))
		{
			startProcess();
			dispose();
		}
		return true;
	}	//	init

	public void startProcess()
	{		
		m_pi.setPrintPreview(true);
		if (m_ASyncProcess != null) {
			m_ASyncProcess.lockUI(m_pi);
		}
		ProcessCtl.process(m_ASyncProcess, m_WindowNo, parameterPanel, m_pi, null);
		dispose();
	}

	public boolean isAsap() {
		return true;
	}

	public void onEvent(Event event) {
		Component component = event.getTarget(); 
		if (component instanceof Button) {
			Button element = (Button)component;
			if ("ok".equalsIgnoreCase(element.getName())) {
				this.startProcess();
				this.dispose();
			} else if ("cancel".equalsIgnoreCase(element.getName())) {
				this.dispose();
			}
		}
		
	}
	
}	//	ProcessDialog