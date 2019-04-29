package org.jsj.my.exception;

import org.jsj.my.constant.ErrorCode;

/**
 * 涵盖所有的数据库错误
 *
 * @author JSJ
 */
public class DatabaseException extends InternalServerException {

    public DatabaseException() {
        super(ErrorCode.DATABASE_ERROR, "Database error");
    }

    public DatabaseException(String msg) {
        super(ErrorCode.DATABASE_ERROR, msg);
    }

    public DatabaseException(int code, String msg) {
        super(code, msg);
    }

}
