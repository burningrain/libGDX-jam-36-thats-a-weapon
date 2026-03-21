package com.github.br.libgdx.jam36.context;

public class TabletContext {

    private int currentPage = 0;
    private String[] pages;
    private String signButtonText;

    public void setPages(String... pages) {
        this.setPages(0, pages);
    }

    public void setPages(int currentPage, String... pages) {
        if (currentPage < 0 || currentPage > (pages.length - 1)) {
            throw new IllegalArgumentException("currentPage=" + currentPage + " pagesCount=" + pages.length);
        }

        this.currentPage = currentPage;
        this.pages = pages;
    }

    public String getCurrentPage() {
        return pages[currentPage];
    }

    public void goToPrevPage() {
        if (currentPage > 0) {
            currentPage--;
            notifyListener();
        }
    }

    public void goToNextPage() {
        if (currentPage < pages.length - 1) {
            currentPage++;
            notifyListener();
        }
    }

    public void setSignButtonText(String signButtonText) {
        this.signButtonText = signButtonText;
    }

    public String getSignButtonText() {
        return signButtonText;
    }


    // listener
    public interface Listener {
        void update(TabletContext context);
    }


    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyListener() {
        if (listener != null) {
            listener.update(this);
        }
    }

}
