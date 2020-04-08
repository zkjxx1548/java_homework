package ktvEntity;

import java.util.Objects;

public class ParkingLot {
    private String id;
    private int carCount;

    public ParkingLot() {
    }

    public ParkingLot(String id, int carCount) {
        this.id = id;
        this.carCount = carCount;
    }

    public String getId() {
        return id;
    }

    public int getCarCount() {
        return carCount;
    }

    public int getIdASCII() {
        return getId().charAt(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return carCount == that.carCount &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carCount);
    }

    @Override
    public String toString() {
        return "ktvEntity.ParkingLot{" +
                "id='" + id + '\'' +
                ", carCount=" + carCount +
                '}';
    }
}
