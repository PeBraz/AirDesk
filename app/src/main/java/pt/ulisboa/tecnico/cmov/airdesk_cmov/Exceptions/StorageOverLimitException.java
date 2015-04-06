package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;

public final class StorageOverLimitException extends Exception {
    private final int maxStorage;
    private final int newStorage;
    private final int minStorage;
    public StorageOverLimitException(int minStorage, int maxStorage, int newStorage) {
        this.minStorage = minStorage;
        this.maxStorage = maxStorage;
        this.newStorage = newStorage;
    }
    @Override
    public final String getMessage() {
        return "Storage must be between "+this.minStorage+" and "+this.maxStorage+
               ". Tried to change it to"+ this.newStorage +".";
    }
}
