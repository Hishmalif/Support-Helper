package org.hishmalif.supporthelperbot.service.sql;

import java.io.File;

public interface SQLService {
    Boolean insertDatabase(File file);
    String getSQLQuery();
}
