/* **************************************************************************
* $Novell: /ldap/src/jldap/com/novell/ldap/client/Message.java,v 1.12 2001/02/26 19:58:27 vtag Exp $
*
 * Copyright (C) 1999, 2000, 2001 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.7 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.7 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************/

package com.novell.ldap.client;

import com.novell.ldap.client.*;
import com.novell.ldap.rfc2251.*;
import com.novell.ldap.*;

import java.util.*;
import java.io.*;

/**
 * Encapsulates an LDAP message, its state, and its replies.
 */
public class Message extends Thread
{
    private LDAPMessage msg;             // msg request sent to server
    private Connection conn;             // Connection object where msg sent
    private MessageAgent agent;          // MessageAgent handling this request
    private LDAPListener listen;         // Application listener
    private int mslimit;                 // client time limit in milliseconds
    // Note: MessageVector is synchronized
    private MessageVector replies = new MessageVector(5,5); // place to store replies
    private int msgId;                   // message ID of this request
    private boolean acceptReplies = true;// false if no longer accepting replies
    private boolean waitForReply = true;   // true if wait for reply
    private boolean complete = false;    // true LDAPResult received
    private String name;                 // String name used for Debug
    private BindProperties bindprops;    // Bind properties if a bind request

    /**
     * Constructs a Message class encapsulating information about this message.
     *
     * @param msg       the message to send to the server
     *<br><br>
     * @param mslimit   number of milliseconds to wait before the message times out.
     *<br><br>
     * @param conn      the connection used to send this message
     *<br><br>
     * @param agent     the MessageAgent handling this message.
     *<br><br>
     * @param listen    the application LDAPListener for this message
     */
    public Message(
                        LDAPMessage    msg,
                        int            mslimit,
                        Connection     conn,
                        MessageAgent   agent,
                        LDAPListener   listen,
                        BindProperties bindprops)
    {
        this.msg = msg;
        this.conn = conn;
        this.agent = agent;
        this.listen = listen;
        this.mslimit = mslimit;
        this.msgId = msg.getMessageID();
        this.bindprops = bindprops;

        if( Debug.LDAP_DEBUG) {
            name = "Message(" + this.msgId + "): ";
            Debug.trace( Debug.messages, name +
                " Created with mslimit " + this.mslimit);
        }
        return;
    }

    /**
     * This method write the message on the wire.  It MUST never be called
     * more than once.  Previously we were sending the message in the
     * constructor, but that opens a small timing window where a reply
     * could return before the code returns and this object gets queued
     * on the MessageAgentQueue.  In that small case, the application
     * would not wake up on the reply.  Making this method separate, closes
     * that window but opens the possibility for misuse.  We do not
     * enforce the requirement that it be called only once as that adds
     * extra synchronization.  We depend on the interal API to act correctly.
     * When the message is sent, the timer thread is started to time
     * the message.
     */
     public void sendMessage()
                throws LDAPException
     {
        if( Debug.LDAP_DEBUG) {
            Debug.trace( Debug.messages, name + "Sending request to " +
                conn.getConnectionName());
        }
        conn.writeMessage( this );
        // Start the timer thread
        if( mslimit != 0 ) {
            // Don't start the timer thread for abandon or Unbind
            switch( msg.getType())
            {
                case LDAPMessage.ABANDON_REQUEST:
                case LDAPMessage.UNBIND_REQUEST:
                    mslimit = 0;
                    break;
                default:
                    this.start();
            }
        }
        return;
    }
    /**
     * Returns true if replies are queued
     *
     * @return false if no replies are queued, otherwise true
     */
    /* package */
    boolean hasReplies()
    {
        return (replies.size() > 0);
    }

    /**
     * Returns true if replies are accepted for this request.
     *
     * @return false if replies are no longer accepted for this request
     */
    /* package */
    boolean acceptsReplies()
    {
        return acceptReplies;
    }

    /**
     * prevents future replies from being accepted for this request
     */
    /* package */
    void refuseReplies()
    {
        acceptReplies = false;
        return;
    }

    /**
     * sets the agent for this message
     */
    /* package */
    void setAgent( MessageAgent agent)
    {
        this.agent = agent;
        return;
    }

    /**
     * stops the timeout timer from running
     */
    /* package */
    void stopTimer()
    {
        // If timer thread started, stop it
        if( mslimit > 0) {
            interrupt();
        }
        return;
    }

    /**
     * Notifies all waiting threads
     */
    private void sleepersAwake()
    {
        if( Debug.LDAP_DEBUG) {
            Debug.trace( Debug.messages, name + "Sleepers Awake, " +
                agent.getAgentName());
        }
        // Notify any thread waiting for this message id
        synchronized( replies) {
            replies.notify();
        }
        // Notify a thread waiting for any message id
        agent.sleepersAwake(false);
        return;
    }

    /**
     * gets the Connection associated with this message
     *
     * @return the Connection associated with this message.
     */
    public Connection getConnection()
    {
        return conn;
    }

    /**
     * gets the operation complete status for this message
     *
     * @return the true if the operation is complete, i.e.
     * the LDAPResult has been received.
     */
    /* package */
    boolean isComplete()
    {
        return complete;
    }

    /**
     * gets the LDAPListener associated with this message
     *
     * @return the LDAPListener associated with this message
     */
    /* package */
    LDAPListener getLDAPListener()
    {
        return listen;
    }

    /**
     * gets the MessageAgent associated with this message
     *
     * @return the MessageAgent associated with this message
     */
    /* package */
    MessageAgent getMessageAgent()
    {
        return agent;
    }

    /**
     * gets the LDAPMessage request associated with this message
     *
     * @return the LDAPMessage request associated with this message
     */
    public LDAPMessage getRequest()
    {
        return msg;
    }

    /**
     * gets the Message ID associated with this message request
     *
     * @return the Message ID associated with this message request
     */
    /* package */
    int getMessageID()
    {
        return msgId;
    }

    /**
     * gets the Message Type associated with this message request
     *
     * @return the Message Type associated with this message request
     */
    /* package */
    int getMessageType()
    {
        return msg.getType();
    }

    /**
     * Puts a reply on the reply queue
     *
     * @param message the RfcLDAPMessage to put on the reply queue.
     */
    /* package */
    void putReply( RfcLDAPMessage message)
    {
        if( ! acceptReplies) {
            if( Debug.LDAP_DEBUG ) {
                Debug.trace( Debug.messages, name +
                    "not accepting replies, discarding reply");
            }
            return;
        }
        replies.addElement( message);
        message.setRequestingMessage( msg); // Save request message info
        if( message.getProtocolOp() instanceof RfcResponse) {
            int res;
            if( Debug.LDAP_DEBUG) {
                res = ((RfcResponse)message.getProtocolOp()).getResultCode().getInt();
                Debug.trace( Debug.messages, name +
                    "Queued LDAPResult (" + replies.size() +
                    " in queue), message complete stopping timer, status " + res);
            }
            stopTimer();
            // Accept no more results for this message
            // Leave on connection queue so we can abandon if necessary
            acceptReplies = false;
            complete = true;
            if( bindprops != null) {
                if( Debug.LDAP_DEBUG) {
                    Debug.trace( Debug.messages, name + "Bind properties found");
                }
                res = ((RfcResponse)message.getProtocolOp()).getResultCode().getInt();
                if(res == LDAPException.SUCCESS) {
                    // Set bind properties into connection object
                    conn.setBindProperties(bindprops);
                    if( Debug.LDAP_DEBUG) {
                        Debug.trace( Debug.messages, name + "Bind status success");
                    }
                } else {
                    if( Debug.LDAP_DEBUG) {
                        Debug.trace( Debug.messages, name + "Bind status " + res);
                    }
                }
                // release the bind semaphore and wake up all waiting threads
                conn.freeBindSemaphore( msgId);
            }
        } else {
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, name +
                    "Reply Queued (" + replies.size() + " in queue)");
            }
        }
        // wake up waiting threads
        sleepersAwake();
        return;
    }

    /**
     * Gets the next reply from the reply queue or waits until one is there
     *
     * @return the next reply message on the reply queue
     */
    /* package */
    Object waitForReply()
    {
        // sync on message so don't confuse with timer thread
        synchronized( replies ) {
            while( waitForReply ) {
                try {
                    Object msg;
                    // We use remove and catch the exception because
                    // it is an atomic get and remove. isEmpty, getFirstElement,
                    // and removeElementAt are multiple statements.
                    // Another thread could remove the object between statements.
                    msg = replies.remove(0); // Atomic get and remove
                    if( Debug.LDAP_DEBUG) {
                        Debug.trace( Debug.messages, name +
                            "Got reply from queue(" +
                            replies.size() + " remaining in queue)");
                    }
                    if( (complete || ! acceptReplies) && replies.isEmpty()) {
                        // Remove msg from connection queue when last reply read
                        conn.removeMessage(this);
                    }
                    return msg;
                } catch( ArrayIndexOutOfBoundsException ex ) {
                    if( Debug.LDAP_DEBUG) {
                        Debug.trace( Debug.messages, name +
                            "No replies queued, waitForReply=" + waitForReply);
                    }
                    if( waitForReply) {
                        try {
                            if( Debug.LDAP_DEBUG) {
                                Debug.trace( Debug.messages, name +
                                    "Wait for a reply");
                            }
                            wait();
                        } catch(InterruptedException ir) {
                            break;
                        }
                    }
                }
            }
            return null;
        }
    }

    /**
     * Gets the next reply from the reply queue if one exists, otherwise
     * throws ArrayIndexOutOfBoundsException
     *
     * @return the next reply message on the reply queue
     *
     * @throws ArrayIndexOutOfBoundsException when no replies exist
     */
    /* package */
    Object getReply()
                throws ArrayIndexOutOfBoundsException
    {
            Object msg;
            // We use remove and catch the exception because
            // it is an atomic get and remove. isEmpty, getFirstElement,
            // and removeElementAt are multiple statements.
            // Another thread could remove the object between statements.
            try {
                msg = replies.remove(0); // Atomic get and remove
                if( Debug.LDAP_DEBUG) {
                    Debug.trace( Debug.messages, name +
                            "Got reply from queue(" +
                            replies.size() + " remaining in queue)");
                }
                if( (complete || ! acceptReplies) && replies.isEmpty()) {
                    // Remove msg from connection queue when last reply read
                    conn.removeMessage(this);
                }
            } catch( ArrayIndexOutOfBoundsException ex ) {
                if( Debug.LDAP_DEBUG) {
                    Debug.trace( Debug.messages, name +
                        "No replies queued for message");
                }
                // ArrayIndexOutOfBoundsException signifies no replies queued
                throw ex;
            }
            return msg;
    }


    /**
     * abandon a request.
     * All queued replies are discarded.  The message is removed
     * from the connection and agent lists. Any client threads waiting
     * on this request are notified.
     *
     * @param cons and LDAPConstraints associated with the abandon.
     *<br><br>
     * @param informUserEx true if user must be informed of operation
     */
    /* package */
    void abandon( LDAPConstraints cons, LocalException informUserEx)
    {
        if( Debug.LDAP_DEBUG) {
            Debug.trace( Debug.messages, name + "Abandon request, complete="
                + complete + ", bind=" + (bindprops != null) +
                ", informUser=" + (informUserEx != null) +
                ", waitForReply=" + waitForReply);
        }
        if( ! waitForReply) {
            return;
        }
        acceptReplies = false;  // don't listen to anyone
        waitForReply = false;   // don't let sleeping threads lie
        if( ! complete) {
            try {
                // If a bind, release bind semaphore & wake up waiting threads
                // Must do before writing abandon message, otherwise deadlock
                if( bindprops != null) {
                    conn.freeBindSemaphore( msgId);
                }
                if( Debug.LDAP_DEBUG) {
                    Debug.trace( Debug.messages, name + "Sending abandon request");
                }
                // Create the abandon message, but don't track it.
                LDAPMessage msg = new LDAPMessage( new RfcAbandonRequest( msgId));
                // Send abandon message to server
                conn.writeMessage( msg);
            } catch (LDAPException ex) {
                ; // do nothing
            }
            complete = true;
            // If not informing user, remove message from agent
            if( informUserEx == null) {
                agent.abandon( msgId, null);
            }
            conn.removeMessage( this);
        }
        // Get rid of all replies queued
        cleanup();
        if( informUserEx != null) {
            replies.addElement( new LDAPResponse( informUserEx,
                    conn.getReferralList(), conn.getActiveReferral()));
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, name +
                        "Queued exception as LDAPResponse (" + replies.size() +
                        " in queue):" +
                        " following referral=" + (conn.getReferralList() != null) +
                        "\n\texception: " + informUserEx.getLDAPErrorMessage());
            }
            // wake up waiting threads
            sleepersAwake();
        } else {
            // Wake up any waiting threads, so they can terminate.
            // If informing the user, we wake sleepers after
            // caller queues dummy response with error status
            sleepersAwake();
        }
        return;
    }

    /**
     * The timeout thread.  If it wakes from the sleep, future input
     * is stopped and the request is timed out.
    */
    public void run()
    {
        try {
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, name + "client timer started, " +
                    mslimit + " milliseconds");
            }
            sleep(mslimit);
            acceptReplies = false;
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, name + "client timed out");
            }
            // Note: Abandon clears the bind semaphore after failed bind.
            abandon( null, new LocalException("Client request timed out", null,
                                LDAPException.LDAP_TIMEOUT, null, this));
        } catch ( InterruptedException ie ) {
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, name + "timer stopped");
            }
            // the timer was stopped, do nothing
        }
        return;
    }

    /**
     * Release reply messages
     */
    /* package */
    void cleanup()
    {
        if( Debug.LDAP_DEBUG) {
            Debug.trace( Debug.messages, name + "cleanup");
        }
        try {
            super.finalize();
            acceptReplies = false;
            if( ! complete) {
                conn.removeMessage( this );
            }
            // Empty out any accumuluated replies
            if( Debug.LDAP_DEBUG) {
                if( ! replies.isEmpty()) {
                    Debug.trace( Debug.messages, name +
                        "cleanup: remove " + replies.size() + " replies");
                }
            }
            while( ! replies.isEmpty()) {
                replies.remove(0);
            }
        } catch ( Throwable ex ) {
            if( Debug.LDAP_DEBUG) {
                Debug.trace( Debug.messages, name +
                    "cleanup exception:" + ex.toString());
            }
            ;// nothing
        }
        return;
    }

    /**
     * finalize
     */
    public void finalize()
    {
        cleanup();
        return;
    }
}
