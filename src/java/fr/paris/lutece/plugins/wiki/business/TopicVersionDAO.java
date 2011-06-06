/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for TopicVersion objects
 */
public final class TopicVersionDAO implements ITopicVersionDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_topic_version ) FROM wiki_topic_version";
    private static final String SQL_QUERY_SELECT = "SELECT id_topic_version, edit_comment, id_topic, lutece_user_id, date_edition, id_topic_version_previous,wiki_content FROM wiki_topic_version WHERE id_topic_version = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_topic_version ( id_topic_version, edit_comment, id_topic, lutece_user_id, date_edition, id_topic_version_previous ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM wiki_topic_version WHERE id_topic_version = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE wiki_topic_version SET id_topic_version = ?, edit_comment = ?, id_topic = ?, lutece_user_id = ?, date_edition = ?, id_topic_version_previous = ? WHERE id_topic_version = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_topic_version, edit_comment, id_topic, lutece_user_id, date_edition, id_topic_version_previous FROM wiki_topic_version";
    private static final String SQL_QUERY_INSERT_MODIFICATION = "INSERT INTO wiki_topic_version ( id_topic_version, edit_comment, id_topic, lutece_user_id, date_edition,wiki_content, id_topic_version_previous ) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_SELECT_LAST_BY_TOPIC_ID = "SELECT id_topic_version, edit_comment, id_topic, lutece_user_id, date_edition, id_topic_version_previous,wiki_content FROM wiki_topic_version WHERE id_topic = ?  ORDER BY  date_edition DESC LIMIT 0,1";
    private static final String SQL_QUERY_SELECT_BY_TOPIC_ID = "SELECT id_topic_version, edit_comment, id_topic, lutece_user_id, date_edition, id_topic_version_previous,wiki_content FROM wiki_topic_version WHERE id_topic = ?  ORDER BY  date_edition DESC ";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     * @param topicVersion instance of the TopicVersion object to insert
     * @param plugin The plugin
     */
    public void insert( TopicVersion topicVersion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        topicVersion.setIdTopicVersion( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, topicVersion.getIdTopicVersion(  ) );
        daoUtil.setString( 2, topicVersion.getEditComment(  ) );
        daoUtil.setInt( 3, topicVersion.getIdTopic(  ) );
        daoUtil.setString( 4, topicVersion.getLuteceUserId(  ) );
        daoUtil.setTimestamp( 5, topicVersion.getDateEdition(  ) );
        daoUtil.setInt( 6, topicVersion.getIdTopicVersionPrevious(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the topicVersion from the table
     * @param nId The identifier of the topicVersion
     * @param plugin The plugin
     * @return the instance of the TopicVersion
     */
    public TopicVersion load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        TopicVersion topicVersion = null;

        if ( daoUtil.next(  ) )
        {
            topicVersion = new TopicVersion(  );

            topicVersion.setIdTopicVersion( daoUtil.getInt( 1 ) );
            topicVersion.setEditComment( daoUtil.getString( 2 ) );
            topicVersion.setIdTopic( daoUtil.getInt( 3 ) );
            topicVersion.setLuteceUserId( daoUtil.getString( 4 ) );
            topicVersion.setDateEdition( daoUtil.getTimestamp( 5 ) );
            topicVersion.setIdTopicVersionPrevious( daoUtil.getInt( 6 ) );
            topicVersion.setWikiContent( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return topicVersion;
    }

    /**
     * Delete a record from the table
     * @param nTopicVersionId The identifier of the topicVersion
     * @param plugin The plugin
     */
    public void delete( int nTopicVersionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nTopicVersionId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param topicVersion The reference of the topicVersion
     * @param plugin The plugin
     */
    public void store( TopicVersion topicVersion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, topicVersion.getIdTopicVersion(  ) );
        daoUtil.setString( 2, topicVersion.getEditComment(  ) );
        daoUtil.setInt( 3, topicVersion.getIdTopic(  ) );
        daoUtil.setString( 4, topicVersion.getLuteceUserId(  ) );
        daoUtil.setTimestamp( 5, topicVersion.getDateEdition(  ) );
        daoUtil.setInt( 6, topicVersion.getIdTopicVersionPrevious(  ) );
        daoUtil.setInt( 7, topicVersion.getIdTopicVersion(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the topicVersions and returns them as a collection
     * @param plugin The plugin
     * @return The Collection which contains the data of all the topicVersions
     */
    public Collection<TopicVersion> selectTopicVersionsList( Plugin plugin )
    {
        Collection<TopicVersion> topicVersionList = new ArrayList<TopicVersion>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TopicVersion topicVersion = new TopicVersion(  );

            topicVersion.setIdTopicVersion( daoUtil.getInt( 1 ) );
            topicVersion.setEditComment( daoUtil.getString( 2 ) );
            topicVersion.setIdTopic( daoUtil.getInt( 3 ) );
            topicVersion.setLuteceUserId( daoUtil.getString( 4 ) );
            topicVersion.setDateEdition( daoUtil.getTimestamp( 5 ) );
            topicVersion.setIdTopicVersionPrevious( daoUtil.getInt( 6 ) );

            topicVersionList.add( topicVersion );
        }

        daoUtil.free(  );

        return topicVersionList;
    }

    public void modifyTopicVersion( int nTopicId, String strUserName, String strComment, String strContent,
        int nPreviousVersion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_MODIFICATION, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setString( 2, strComment );
        daoUtil.setInt( 3, nTopicId );
        daoUtil.setString( 4, strUserName );
        daoUtil.setTimestamp( 5, new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setString( 6, strContent );
        daoUtil.setInt( 7, nPreviousVersion );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    public TopicVersion loadLastVersion( int nIdTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_BY_TOPIC_ID, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeQuery(  );

        TopicVersion topicVersion = null;

        if ( daoUtil.next(  ) )
        {
            topicVersion = new TopicVersion(  );

            topicVersion.setIdTopicVersion( daoUtil.getInt( 1 ) );
            topicVersion.setEditComment( daoUtil.getString( 2 ) );
            topicVersion.setIdTopic( daoUtil.getInt( 3 ) );
            topicVersion.setLuteceUserId( daoUtil.getString( 4 ) );
            topicVersion.setDateEdition( daoUtil.getTimestamp( 5 ) );
            topicVersion.setIdTopicVersionPrevious( daoUtil.getInt( 6 ) );
            topicVersion.setWikiContent( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return topicVersion;
    }

    public Collection<TopicVersion> loadAllVersions( int nIdTopic, Plugin plugin )
    {
        Collection<TopicVersion> topicVersionList = new ArrayList<TopicVersion>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_TOPIC_ID, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TopicVersion topicVersion = new TopicVersion(  );

            topicVersion.setIdTopicVersion( daoUtil.getInt( 1 ) );
            topicVersion.setEditComment( daoUtil.getString( 2 ) );
            topicVersion.setIdTopic( daoUtil.getInt( 3 ) );
            topicVersion.setLuteceUserId( daoUtil.getString( 4 ) );
            topicVersion.setDateEdition( daoUtil.getTimestamp( 5 ) );
            topicVersion.setIdTopicVersionPrevious( daoUtil.getInt( 6 ) );

            topicVersionList.add( topicVersion );
        }

        daoUtil.free(  );

        return topicVersionList;
    }
}
