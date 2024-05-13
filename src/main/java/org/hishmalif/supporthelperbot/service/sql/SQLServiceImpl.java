package org.hishmalif.supporthelperbot.service.sql;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
public class SQLServiceImpl implements SQLService {
    private final SQLWorker worker;

    @Override
    public Boolean insertDatabase(File file) {
        final String name;
        if (file == null) {
            return false;
        }
        name = file.getName();

        if (!(name.endsWith(".xls") || name.endsWith(".xlsx"))) {
            return false;
        }
        return worker.insertDatabase(file);
    }

    @Override
    public String getSQLQuery() {
        return ""; // TODO in future
    }
}