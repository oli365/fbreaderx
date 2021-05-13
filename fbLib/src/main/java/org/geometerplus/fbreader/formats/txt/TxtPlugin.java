package org.geometerplus.fbreader.formats.txt;

import org.geometerplus.fbreader.book.BookUtil;
import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.formats.BookReadingException;
import org.geometerplus.fbreader.formats.NativeFormatPlugin;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;

import java.util.Collections;
import java.util.List;

public class TxtPlugin extends NativeFormatPlugin {
    public TxtPlugin(org.geometerplus.zlibrary.core.util.SystemInfo systemInfo) {
        super(systemInfo, "txt");
    }

    @Override
    public void readModel(BookModel model) throws BookReadingException {
        final ZLFile file = BookUtil.fileByBook(model.Book);
        file.setCached(true);
        try {
            super.readModel(model);
            model.setLabelResolver(new BookModel.LabelResolver() {
                public List<String> getCandidates(String id) {
                    final int index = id.indexOf("#");
                    return index > 0
                            ? Collections.<String>singletonList(id.substring(0, index))
                            : Collections.<String>emptyList();
                }
            });
        } finally {
            file.setCached(false);
        }
    }
}
