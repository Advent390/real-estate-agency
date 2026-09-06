package com.metrazh.agency.dto;

/** Статистика для адмін-дашборду. Аналог db.get_stats(). */
public class DashboardStats {

    private final long totalObjects;
    private final long activeObjects;
    private final long archivedObjects;
    private final long totalClients;
    private final long pendingViewings;

    public DashboardStats(long totalObjects, long activeObjects, long archivedObjects,
                           long totalClients, long pendingViewings) {
        this.totalObjects = totalObjects;
        this.activeObjects = activeObjects;
        this.archivedObjects = archivedObjects;
        this.totalClients = totalClients;
        this.pendingViewings = pendingViewings;
    }

    public long getTotalObjects() {
        return totalObjects;
    }

    public long getActiveObjects() {
        return activeObjects;
    }

    public long getArchivedObjects() {
        return archivedObjects;
    }

    public long getTotalClients() {
        return totalClients;
    }

    public long getPendingViewings() {
        return pendingViewings;
    }
}
