package com.alibaba.polardbx.qatest.ddl.sharding.omc;

import com.alibaba.polardbx.executor.common.StorageInfoManager;
import com.alibaba.polardbx.qatest.util.ConnectionManager;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

public class ConcurrentInsertTest extends ConcurrentDMLBaseTest {
    private final boolean supportsAlterType =
        StorageInfoManager.checkSupportAlterType(ConnectionManager.getInstance().getMysqlDataSource());
    private final boolean isRDS80 = StorageInfoManager.checkRDS80(ConnectionManager.getInstance().getMysqlDataSource());

    @Before
    public void beforeMethod() {
        org.junit.Assume.assumeTrue(supportsAlterType && !isRDS80);
    }

    @Override
    public boolean usingNewPartDb() {
        return false;
    }

    @Test
    public void modifyWithInsert1() throws Exception {
        String tableName = "omc_with_insert_1";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d+1)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyWithInsert2() throws Exception {
        String tableName = "omc_with_insert_2";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator = (count) -> String.format("insert into %%s(a) values (%d)", count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colB == 0;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyWithInsert3() throws Exception {
        String tableName = "omc_with_insert_3";
        String colDef = "int not null";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint not null";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d+1)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyWithInsert4() throws Exception {
        String tableName = "omc_with_insert_4";
        String colDef = "int not null";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint not null";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s values (%d,%d+1,null,null)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyWithInsert5() throws Exception {
        String tableName = "omc_with_insert_5";
        String colDef = "int not null default 3";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint not null default 4";
        String selectSql = "select * from %s";
        Function<Integer, String> generator = (count) -> String.format("insert into %%s(a) values (%d)", count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colB == 3 || colB == 4);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyWithInsert6() throws Exception {
        String tableName = "omc_with_insert_6";
        String colDef = "int default 3";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint default null";
        String selectSql = "select * from %s";
        Function<Integer, String> generator = (count) -> String.format("insert into %%s(a) values (%d)", count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colB == 3 || colB == 0);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyWithInsert7() throws Exception {
        String tableName = "omc_with_insert_7";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d),(%d,%d)", count * 2, count * 2 + 1,
                count * 2 + 1, count * 2 + 2);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            2);
    }

    @Test
    public void modifyWithInsert8() throws Exception {
        String tableName = "omc_with_insert_8";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b) values (%d*2,%d*2+1),(%d*2+1,%d*2+2)", count, count,
                count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            2);
    }

    @Test
    public void modifyMultiWithInsert1() throws Exception {
        String tableName = "omc_multi_with_insert_1";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint, change column d dd varchar(20) default 'abc' after b";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d+1)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB && colC.equalsIgnoreCase(colD));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyMultiWithInsert2() throws Exception {
        String tableName = "omc_multi_with_insert_2";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint, modify column d varchar(20)";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b,c,d) values (%d,%d+1,%d,%d+1)", count, count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB && Float.parseFloat(colC) + 1 == Float.parseFloat(colD));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyMultiWithInsert3() throws Exception {
        String tableName = "omc_multi_with_insert_3";
        String colDef = "int not null";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint, modify column d varchar(2)";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b,c,d) values (%d,%d+1,%d,%d)", count, count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB && (colC.equals(colD) || colC.substring(0, 2).equals(colD)));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1, false);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1, false);
    }

    @Test
    public void modifyMultiWithInsert4() throws Exception {
        String tableName = "omc_multi_with_insert_4";
        String colDef = "int not null default 3";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint, modify column d varchar(10) default 'bcd'";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s(a,b,c) values (%d,%d+1,%d)", count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB && Float.parseFloat(colC) == colA && (
                colD.equalsIgnoreCase("abc") || colD.equalsIgnoreCase("bcd")));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void modifyMultiWithInsert5() throws Exception {
        String tableName = "omc_multi_with_insert_5";
        String colDef = "int not null default 3";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s modify column b bigint, drop column c, add column cc varchar(10) default 'efg' after b";
        String selectSql = "select * from %s";
        Function<Integer, String> generator =
            (count) -> String.format("insert into %%s values (%d,%d+1,%d,%d)", count, count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB && (colC.equalsIgnoreCase("efg")
                || colC.equalsIgnoreCase("abc") || Float.parseFloat(colC) == colA));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator, generator, checker, false, false,
            1);
    }

    @Test
    public void changeWithInsert1() throws Exception {
        String tableName = "omc_with_insert_1";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d+1)", count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format("insert into %%s(a,e) values (%d,%d+1)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 1);
    }

    @Test
    public void changeWithInsert2() throws Exception {
        String tableName = "omc_with_insert_2";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 = (count) -> String.format("insert into %%s(a) values (%d)", count);
        Function<Integer, String> generator2 = (count) -> String.format("insert into %%s(a) values (%d)", count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colB == 0;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 1);
    }

    @Test
    public void changeWithInsert3() throws Exception {
        String tableName = "omc_with_insert_3";
        String colDef = "int not null";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint not null";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d+1)", count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format("insert into %%s(a,e) values (%d,%d+1)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 1);
    }

    @Test
    public void changeWithInsert4() throws Exception {
        String tableName = "omc_with_insert_4";
        String colDef = "int not null";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint not null";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format("insert into %%s values (%d,%d+1,null,null)", count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format("insert into %%s values (%d,%d+1,null,null)", count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 1);
    }

    @Test
    public void changeWithInsert5() throws Exception {
        String tableName = "omc_with_insert_5";
        String colDef = "int not null default 3";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint not null default 4";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 = (count) -> String.format("insert into %%s(a) values (%d)", count);
        Function<Integer, String> generator2 = (count) -> String.format("insert into %%s(a) values (%d)", count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colB == 3 || colB == 4);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 1);
    }

    @Test
    public void changeWithInsert6() throws Exception {
        String tableName = "omc_with_insert_6";
        String colDef = "int default 3";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint default null";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 = (count) -> String.format("insert into %%s(a) values (%d)", count);
        Function<Integer, String> generator2 = (count) -> String.format("insert into %%s(a) values (%d)", count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colB == 3 || colB == 0);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            1);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 1);
    }

    @Test
    public void changeWithInsert7() throws Exception {
        String tableName = "omc_with_insert_7";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format("insert into %%s(a,b) values (%d,%d),(%d,%d)", count * 2, count * 2 + 1,
                count * 2 + 1, count * 2 + 2);
        Function<Integer, String> generator2 =
            (count) -> String.format("insert into %%s(a,e) values (%d,%d),(%d,%d)", count * 2, count * 2 + 1,
                count * 2 + 1, count * 2 + 2);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 2);
    }

    @Test
    public void changeWithInsert8() throws Exception {
        String tableName = "omc_with_insert_8";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format("insert into %%s(a,b) values (%d*2,%d*2+1),(%d*2+1,%d*2+2)", count, count,
                count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format("insert into %%s(a,e) values (%d*2,%d*2+1),(%d*2+1,%d*2+2)", count, count,
                count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker = (colA, colB, colC, colD) -> colA + 1 == colB;
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 2);
    }

    @Test
    public void changeMultiWithInsert1() throws Exception {
        String tableName = "omc_multi_with_insert_1";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint, change column c cc varchar(20) default 'new default', drop column d, add column f varchar(10) not null default 'new column'";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format("insert into %%s(a,b) values (%d*2,%d*2+1),(%d*2+1,%d*2+2)", count,
                count, count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format("insert into %%s(a,e) values (%d*2,%d*2+1),(%d*2+1,%d*2+2)", count,
                count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB && !(colC.equalsIgnoreCase("new default")
                && colD.equalsIgnoreCase("abc")));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 2);
    }

    @Test
    public void changeMultiWithInsert2() throws Exception {
        String tableName = "omc_multi_with_insert_2";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint, change column c d varchar(20), change column d c varchar(20)";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format(
                "insert into %%s(a,b,c,d) values (%d*2,%d*2+1,%d*2,%d*2+1),(%d*2+1,%d*2+2,%d*2,%d*2+1)",
                count, count, count, count, count, count, count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format(
                "insert into %%s(a,e,d,c) values (%d*2,%d*2+1,%d*2,%d*2+1),(%d*2+1,%d*2+2,%d*2,%d*2+1)",
                count, count, count, count, count, count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB) && (Float.parseFloat(colC) + 1 == Float.parseFloat(colD));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 2);
    }

    @Test
    public void changeMultiWithInsert3() throws Exception {
        String tableName = "omc_multi_with_insert_3";
        String colDef = "int";
        String alterSql = buildCmdExtra(OMC_FORCE_TYPE_CONVERSION)
            + " alter table %s change column b e bigint, change column c d int, change column d c int";
        String selectSql = "select * from %s";
        Function<Integer, String> generator1 =
            (count) -> String.format(
                "insert into %%s(a,b,c,d) values (%d*2,%d*2+1,%d*2,%d*2+1),(%d*2+1,%d*2+2,%d*2,%d*2+1)",
                count, count, count, count, count, count, count, count);
        Function<Integer, String> generator2 =
            (count) -> String.format(
                "insert into %%s(a,e,d,c) values (%d*2,%d*2+1,%d*2,%d*2+1),(%d*2+1,%d*2+2,%d*2,%d*2+1)",
                count, count, count, count, count, count, count, count);
        QuadFunction<Integer, Integer, String, String, Boolean> checker =
            (colA, colB, colC, colD) -> (colA + 1 == colB) && (Float.parseFloat(colC) + 1 == Float.parseFloat(colD));
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false, true,
            2);
        concurrentTestInternal(tableName, colDef, alterSql, selectSql, generator1, generator2, checker, false,
            false, 2);
    }
}
