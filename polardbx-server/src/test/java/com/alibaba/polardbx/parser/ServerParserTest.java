/*
 * Copyright [2013-2021], Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.polardbx.parser;

import com.alibaba.polardbx.server.parser.ServerParse;
import com.alibaba.polardbx.server.parser.ServerParseSelect;
import com.alibaba.polardbx.server.parser.ServerParseSet;
import com.alibaba.polardbx.server.parser.ServerParseShow;
import com.alibaba.polardbx.server.parser.ServerParseStart;
import com.alibaba.polardbx.druid.sql.parser.ByteString;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xianmao.hexm
 */
public class ServerParserTest {

    @Test
    public void testIsBegin() {
        Assert.assertEquals(ServerParse.BEGIN, ServerParse.parse("begin"));
        Assert.assertEquals(ServerParse.BEGIN, ServerParse.parse("BEGIN"));
        Assert.assertEquals(ServerParse.BEGIN, ServerParse.parse("BegIn"));
    }

    @Test
    public void testIsCommit() {
        Assert.assertEquals(ServerParse.COMMIT, ServerParse.parse("commit"));
        Assert.assertEquals(ServerParse.COMMIT, ServerParse.parse("COMMIT"));
        Assert.assertEquals(ServerParse.COMMIT, ServerParse.parse("cOmmiT "));
    }

    @Test
    public void testIsDelete() {
        Assert.assertEquals(ServerParse.DELETE, ServerParse.parse("delete ..."));
        Assert.assertEquals(ServerParse.DELETE, ServerParse.parse("DELETE ..."));
        Assert.assertEquals(ServerParse.DELETE, ServerParse.parse("DeletE ..."));
    }

    @Test
    public void testIsInsert() {
        Assert.assertEquals(ServerParse.INSERT, ServerParse.parse("insert ..."));
        Assert.assertEquals(ServerParse.INSERT, ServerParse.parse("INSERT ..."));
        Assert.assertEquals(ServerParse.INSERT, ServerParse.parse("InserT ..."));
    }

    @Test
    public void testIsReplace() {
        Assert.assertEquals(ServerParse.REPLACE, ServerParse.parse("replace ..."));
        Assert.assertEquals(ServerParse.REPLACE, ServerParse.parse("REPLACE ..."));
        Assert.assertEquals(ServerParse.REPLACE, ServerParse.parse("rEPLACe ..."));
    }

    @Test
    public void testIsRollback() {
        Assert.assertEquals(ServerParse.ROLLBACK, ServerParse.parse("rollback"));
        Assert.assertEquals(ServerParse.ROLLBACK, ServerParse.parse("ROLLBACK"));
        Assert.assertEquals(ServerParse.ROLLBACK, ServerParse.parse("rolLBACK "));
    }

    @Test
    public void testIsSelect() {
        Assert.assertEquals(ServerParse.SELECT, 0xff & ServerParse.parse("select ..."));
        Assert.assertEquals(ServerParse.SELECT, 0xff & ServerParse.parse("SELECT ..."));
        Assert.assertEquals(ServerParse.SELECT, 0xff & ServerParse.parse("sELECt ..."));
    }

    @Test
    public void testIsSet() {
        Assert.assertEquals(ServerParse.SET, 0xff & ServerParse.parse("set ..."));
        Assert.assertEquals(ServerParse.SET, 0xff & ServerParse.parse("SET ..."));
        Assert.assertEquals(ServerParse.SET, 0xff & ServerParse.parse("sEt ..."));
    }

    @Test
    public void testIsShow() {
        Assert.assertEquals(ServerParse.SHOW, 0xff & ServerParse.parse("show ..."));
        Assert.assertEquals(ServerParse.SHOW, 0xff & ServerParse.parse("SHOW ..."));
        Assert.assertEquals(ServerParse.SHOW, 0xff & ServerParse.parse("sHOw ..."));
    }

    @Test
    public void testIsStart() {
        Assert.assertEquals(ServerParse.START, 0xff & ServerParse.parse("start ..."));
        Assert.assertEquals(ServerParse.START, 0xff & ServerParse.parse("START ..."));
        Assert.assertEquals(ServerParse.START, 0xff & ServerParse.parse("stART ..."));
    }

    @Test
    public void testIsUpdate() {
        Assert.assertEquals(ServerParse.UPDATE, ServerParse.parse("update ..."));
        Assert.assertEquals(ServerParse.UPDATE, ServerParse.parse("UPDATE ..."));
        Assert.assertEquals(ServerParse.UPDATE, ServerParse.parse("UPDate ..."));
    }

    @Test
    public void testIsShowDatabases() {
        Assert.assertEquals(ServerParseShow.DATABASES, ServerParseShow.parse("show databases", 4));
        Assert.assertEquals(ServerParseShow.DATABASES, ServerParseShow.parse("SHOW DATABASES", 4));
        Assert.assertEquals(ServerParseShow.DATABASES, ServerParseShow.parse("SHOW databases ", 4));
    }

    @Test
    public void testIsShowGitCommit() {
        Assert.assertEquals(ServerParseShow.GIT_COMMIT, ServerParseShow.parse("show git_commit", 4));
        Assert.assertEquals(ServerParseShow.GIT_COMMIT, ServerParseShow.parse("show GIT_COMMIT", 4));
        Assert.assertEquals(ServerParseShow.GIT_COMMIT, ServerParseShow.parse("show GIT_COmmIT", 4));
    }

    @Test
    public void testIsShowDataSources() {
        Assert.assertEquals(ServerParseShow.DATASOURCES, ServerParseShow.parse("show datasources", 4));
        Assert.assertEquals(ServerParseShow.DATASOURCES, ServerParseShow.parse("SHOW DATASOURCES", 4));
        Assert.assertEquals(ServerParseShow.DATASOURCES, ServerParseShow.parse("  SHOW   DATASOURCES  ", 6));
    }

    // @Test
    public void testShowCobarStatus() {
        Assert.assertEquals(ServerParseShow.COBAR_STATUS, ServerParseShow.parse("show cobar_status", 4));
        Assert.assertEquals(ServerParseShow.COBAR_STATUS, ServerParseShow.parse("show cobar_status ", 4));
        Assert
            .assertEquals(ServerParseShow.COBAR_STATUS, ServerParseShow.parse(" SHOW COBAR_STATUS", " SHOW".length()));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse(" show cobar_statu", " SHOW".length()));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse(" show cobar_status2", " SHOW".length()));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse("Show cobar_status2 ", "SHOW".length()));
    }

    // @Test
    public void testShowCobarCluster() {
        Assert.assertEquals(ServerParseShow.COBAR_CLUSTER, ServerParseShow.parse("show cobar_cluster", 4));
        Assert.assertEquals(ServerParseShow.COBAR_CLUSTER, ServerParseShow.parse("Show cobar_CLUSTER ", 4));
        Assert.assertEquals(ServerParseShow.COBAR_CLUSTER, ServerParseShow.parse(" show  COBAR_cluster", 5));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse(" show cobar_clust", 5));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse(" show cobar_cluster2", 5));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse("Show COBAR_cluster9 ", 4));
    }

    @Test
    public void testIsShowOther() {
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse("show ...", 4));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse("SHOW ...", 4));
        Assert.assertEquals(ServerParseShow.OTHER, ServerParseShow.parse("SHOW ... ", 4));
    }

    @Test
    public void testIsSetAutocommitOn() {
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_ON, ServerParseSet.parse("set autocommit=1", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_ON, ServerParseSet.parse("set autoCOMMIT = 1", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_ON, ServerParseSet.parse("SET AUTOCOMMIT=on", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_ON, ServerParseSet.parse("set autoCOMMIT = ON", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_ON, ServerParseSet.parse("set autoCOMMIT=true", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_ON, ServerParseSet.parse("set autoCOMMIT = TRUE", 3));
    }

    @Test
    public void testIsSetAutocommitOff() {
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_OFF, ServerParseSet.parse("set autocommit=0", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_OFF, ServerParseSet.parse("SET AUTOCOMMIT= 0", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_OFF, ServerParseSet.parse("set autoCOMMIT =OFF", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_OFF, ServerParseSet.parse("set autoCOMMIT = off", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_OFF, ServerParseSet.parse("set autoCOMMIT=false", 3));
        Assert.assertEquals(ServerParseSet.AUTOCOMMIT_OFF, ServerParseSet.parse("set autoCOMMIT = FALSE", 3));
    }

    @Test
    public void testIsSetNames() {
        Assert.assertEquals(ServerParseSet.NAMES, 0xff & ServerParseSet.parse("set names utf8", 3));
        Assert.assertEquals(ServerParseSet.NAMES, 0xff & ServerParseSet.parse("SET NAMES UTF8", 3));
        Assert.assertEquals(ServerParseSet.NAMES, 0xff & ServerParseSet.parse("set NAMES utf8", 3));
    }

    @Test
    public void testIsCharacterSetResults() {
        Assert.assertEquals(ServerParseSet.CHARACTER_SET_RESULTS,
            0xff & ServerParseSet.parse("SET character_set_results  = NULL", 3));
        Assert.assertEquals(ServerParseSet.CHARACTER_SET_RESULTS,
            0xff & ServerParseSet.parse("SET CHARACTER_SET_RESULTS= NULL", 3));
        Assert.assertEquals(ServerParseSet.CHARACTER_SET_RESULTS,
            0xff & ServerParseSet.parse("Set chARActer_SET_RESults =  NULL", 3));
        Assert.assertEquals(ServerParseSet.CHARACTER_SET_CONNECTION,
            0xff & ServerParseSet.parse("Set chARActer_SET_Connection =  NULL", 3));
        Assert.assertEquals(ServerParseSet.CHARACTER_SET_CLIENT,
            0xff & ServerParseSet.parse("Set chARActer_SET_client =  NULL", 3));
    }

    @Test
    public void testIsSetOther() {
        Assert.assertEquals(ServerParseSet.OTHER, ServerParseSet.parse("set ...", 3));
        Assert.assertEquals(ServerParseSet.OTHER, ServerParseSet.parse("SET ...", 3));
        Assert.assertEquals(ServerParseSet.OTHER, ServerParseSet.parse("sEt ...", 3));
    }

    @Test
    public void testIsKill() {
        Assert.assertEquals(ServerParse.KILL, 0xff & ServerParse.parse(" kill  ..."));
        Assert.assertEquals(ServerParse.KILL, 0xff & ServerParse.parse("kill 111111 ..."));
        Assert.assertEquals(ServerParse.KILL, 0xff & ServerParse.parse("KILL  1335505632"));
    }

    @Test
    public void testIsKillQuery() {
        Assert.assertEquals(ServerParse.KILL_QUERY, 0xff & ServerParse.parse(" kill query ..."));
        Assert.assertEquals(ServerParse.KILL_QUERY, 0xff & ServerParse.parse("kill   query 111111 ..."));
        Assert.assertEquals(ServerParse.KILL_QUERY, 0xff & ServerParse.parse("KILL QUERY 1335505632"));
    }

    @Test
    public void testIsSavepoint() {
        Assert.assertEquals(ServerParse.SAVEPOINT, ServerParse.parse(" savepoint  ..."));
        Assert.assertEquals(ServerParse.SAVEPOINT, ServerParse.parse("SAVEPOINT "));
        Assert.assertEquals(ServerParse.SAVEPOINT, ServerParse.parse(" SAVEpoint   a"));
    }

    @Test
    public void testIsUse() {
        Assert.assertEquals(ServerParse.USE, 0xff & ServerParse.parse(" use  ..."));
        Assert.assertEquals(ServerParse.USE, 0xff & ServerParse.parse("USE "));
        Assert.assertEquals(ServerParse.USE, 0xff & ServerParse.parse(" Use   a"));
    }

    @Test
    public void testIsStartTransaction() {
        Assert.assertEquals(ServerParseStart.TRANSACTION, ServerParseStart.parse(" start transaction  ", 6));
        Assert.assertEquals(ServerParseStart.TRANSACTION, ServerParseStart.parse("START TRANSACTION", 5));
        Assert.assertEquals(ServerParseStart.TRANSACTION, ServerParseStart.parse(" staRT   TRANSaction  ", 6));
    }

    @Test
    public void testIsSelectVersionComment() {
        Assert.assertEquals(ServerParseSelect.VERSION_COMMENT,
            ServerParseSelect.parse(" select @@version_comment  ", 7, null));
        Assert.assertEquals(ServerParseSelect.VERSION_COMMENT,
            ServerParseSelect.parse("SELECT @@VERSION_COMMENT", 6, null));
        Assert.assertEquals(ServerParseSelect.VERSION_COMMENT,
            ServerParseSelect.parse(" selECT    @@VERSION_comment  ", 7, null));
    }

    @Test
    public void testIsSelectVersion() {
        Assert.assertEquals(ServerParseSelect.VERSION, ServerParseSelect.parse(" select version ()  ", 7, null));
        Assert.assertEquals(ServerParseSelect.VERSION, ServerParseSelect.parse("SELECT VERSION(  )", 6, null));
        Assert.assertEquals(ServerParseSelect.VERSION, ServerParseSelect.parse(" selECT    VERSION()  ", 7, null));
    }

    @Test
    public void testIsSelectDatabase() {
        Assert.assertEquals(ServerParseSelect.DATABASE, ServerParseSelect.parse(" select database()  ", 7, null));
        Assert.assertEquals(ServerParseSelect.DATABASE, ServerParseSelect.parse("SELECT DATABASE()", 6, null));
        Assert.assertEquals(ServerParseSelect.DATABASE, ServerParseSelect.parse(" selECT    DATABASE()  ", 7, null));
    }

    @Test
    public void testIsSelectUser() {
        Assert.assertEquals(ServerParseSelect.USER, ServerParseSelect.parse(" select user()  ", 7, null));
        Assert.assertEquals(ServerParseSelect.USER, ServerParseSelect.parse("SELECT USER()", 6, null));
        Assert.assertEquals(ServerParseSelect.USER, ServerParseSelect.parse(" selECT    USER()  ", 7, null));
    }

    @Test
    public void testIsStartMaster() {
        Assert.assertEquals(ServerParse.START_MASTER, 0xff & ServerParse.parse("start master ..."));
        Assert.assertEquals(ServerParse.START_MASTER, 0xff & ServerParse.parse("START MASTER ..."));
    }

    @Test
    public void testIsStartSlave() {
        Assert.assertEquals(ServerParse.START_SLAVE, 0xff & ServerParse.parse("start slave ..."));
        Assert.assertEquals(ServerParse.START_SLAVE, 0xff & ServerParse.parse("START SLAVE ..."));
    }

    @Test
    public void testTxReadUncommitted() {
        Assert.assertEquals(ServerParseSet.TX_READ_UNCOMMITTED,
            ServerParseSet.parse("  SET SESSION TRANSACTION ISOLATION LEVEL READ  UNCOMMITTED  ", "  SET".length()));
        Assert.assertEquals(ServerParseSet.TX_READ_UNCOMMITTED,
            ServerParseSet.parse(" set session transaction isolation level read  uncommitted  ", " SET".length()));
        Assert.assertEquals(ServerParseSet.TX_READ_UNCOMMITTED,
            ServerParseSet.parse(" set session transaCTION ISOLATION LEvel read  uncommitteD ", " SET".length()));
    }

    @Test
    public void testTxReadCommitted() {
        Assert.assertEquals(ServerParseSet.TX_READ_COMMITTED,
            ServerParseSet.parse("  SET SESSION TRANSACTION ISOLATION LEVEL READ  COMMITTED  ", "  SET".length()));
        Assert.assertEquals(ServerParseSet.TX_READ_COMMITTED,
            ServerParseSet.parse(" set session transaction isolation level read  committed  ", " SET".length()));
        Assert.assertEquals(ServerParseSet.TX_READ_COMMITTED,
            ServerParseSet.parse(" set session transaCTION ISOLATION LEVel read  committed ", " SET".length()));
    }

    @Test
    public void testTxRepeatedRead() {
        Assert.assertEquals(ServerParseSet.TX_REPEATABLE_READ,
            ServerParseSet.parse("  SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE   READ  ", "  SET".length()));
        Assert.assertEquals(ServerParseSet.TX_REPEATABLE_READ,
            ServerParseSet.parse(" set session transaction isolation level repeatable   read  ", " SET".length()));
        Assert.assertEquals(ServerParseSet.TX_REPEATABLE_READ,
            ServerParseSet.parse(" set session transaction isOLATION LEVEL REPEatable   read ", " SET".length()));
    }

    @Test
    public void testTxSerializable() {
        Assert.assertEquals(ServerParseSet.TX_SERIALIZABLE,
            ServerParseSet.parse("  SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE  ", "  SET".length()));
        Assert.assertEquals(ServerParseSet.TX_SERIALIZABLE,
            ServerParseSet.parse(" set session transaction   isolation level serializable  ", " SET".length()));
        Assert.assertEquals(ServerParseSet.TX_SERIALIZABLE,
            ServerParseSet.parse(" set session   transaction  isOLATION LEVEL SERIAlizable ", " SET".length()));
    }

    @Test
    public void testIdentity() {
        String stmt = "select @@identity";
        int indexAfterLastInsertIdFunc = ServerParseSelect.indexAfterIdentity(ByteString.from(stmt), stmt.indexOf('i'));
        Assert.assertEquals(stmt.length(), indexAfterLastInsertIdFunc);
        Assert.assertEquals(ServerParseSelect.IDENTITY, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select  @@identity as id";
        Assert.assertEquals(ServerParseSelect.IDENTITY, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select  @@identitY  id";
        Assert.assertEquals(ServerParseSelect.IDENTITY, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select  /*foo*/@@identitY  id";
        Assert.assertEquals(ServerParseSelect.IDENTITY, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select/*foo*/ @@identitY  id";
        Assert.assertEquals(ServerParseSelect.IDENTITY, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select/*foo*/ @@identitY As id";
        Assert.assertEquals(ServerParseSelect.IDENTITY, ServerParseSelect.parse(stmt, 6, null));

        stmt = "select  @@identity ,";
        Assert.assertEquals(ServerParseSelect.OTHER, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select  @@identity as, ";
        Assert.assertEquals(ServerParseSelect.OTHER, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select  @@identity as id  , ";
        Assert.assertEquals(ServerParseSelect.OTHER, ServerParseSelect.parse(stmt, 6, null));
        stmt = "select  @@identity ass id   ";
        Assert.assertEquals(ServerParseSelect.OTHER, ServerParseSelect.parse(stmt, 6, null));

    }

    @Test
    public void testCurrentTrans() {
        Assert.assertEquals(ServerParseSelect.CURRENT_TRANS_ID,
            ServerParseSelect.parse("select current_trans_id();", 6, null));
        Assert.assertEquals(ServerParseSelect.CURRENT_TRANS_ID,
            ServerParseSelect.parse("select current_trans_id()", 6, null));
        Assert.assertEquals(ServerParseSelect.CURRENT_TRANS_ID,
            ServerParseSelect.parse("select current_trans_id(  )   ", 6, null));

        Assert.assertEquals(ServerParseSelect.OTHER,
            ServerParseSelect.parse("select current_trans_id from a", 6, null));
        Assert.assertEquals(ServerParseSelect.OTHER,
            ServerParseSelect.parse("select current_trans_id", 6, null));
    }

    @Test
    public void testPolardbVersion() {
        Assert.assertEquals(ServerParseSelect.POLARDB_VERSION,
            ServerParseSelect.parse("select polardb_version();", 6, null));
        Assert.assertEquals(ServerParseSelect.POLARDB_VERSION,
            ServerParseSelect.parse("select polardb_version()", 6, null));
        Assert.assertEquals(ServerParseSelect.POLARDB_VERSION,
            ServerParseSelect.parse("select polardb_version(  )   ", 6, null));

        Assert.assertEquals(ServerParseSelect.OTHER,
            ServerParseSelect.parse("select polardb_version from a", 6, null));
        Assert.assertEquals(ServerParseSelect.OTHER,
            ServerParseSelect.parse("select polardb_version", 6, null));
    }
}
