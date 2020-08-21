package com.example.template.page;


public class Pagination {

    private int page;
    private int size;

    public Pagination(Builder builder) {
        this.size = builder.size;
        this.page = builder.page;
    }

    public static class Builder {
        private static final int DEFAULT_PAGE = 1;
        private static final int DEFAULT_SIZE = 10;
        private int page;
        private int size;

        public Pagination build() {
            return new Pagination(this);
        }

        public Builder setPage(int page) {
            if (page <= 0) {
                this.page = DEFAULT_PAGE;
            } else {
                this.page = page;
            }
            return this;
        }

        public Builder setSize(int size) {
            if (size <= 0) {
                this.size = DEFAULT_SIZE;
            } else {
                this.size = size;
            }
            return this;
        }
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
