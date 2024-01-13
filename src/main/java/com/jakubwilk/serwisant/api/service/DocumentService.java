package com.jakubwilk.serwisant.api.service;

import java.io.File;

public interface DocumentService {
    File getTempPdfFromAString(String source, String prefix, String suffix);
}
