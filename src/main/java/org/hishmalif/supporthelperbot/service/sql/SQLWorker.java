package org.hishmalif.supporthelperbot.service.sql;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;

@FeignClient(name = "sqlWorker", url = "${sql.url}")
public interface SQLWorker {
    @PostMapping("/database")
    Boolean insertDatabase(File database);
}