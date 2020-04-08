package ktvEntity;

import java.util.Objects;

public class Parking {
    private String ParkingLotId;
    private int id;
    private String carNumber;

    public Parking() {
    }

    public Parking(String parkingLotId, int id, String carNumber) {
        ParkingLotId = parkingLotId;
        this.id = id;
        this.carNumber = carNumber;
    }

    public String getParkingLotId() {
        return ParkingLotId;
    }

    public int getId() {
        return id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parking parking = (Parking) o;
        return id == parking.id &&
                Objects.equals(ParkingLotId, parking.ParkingLotId) &&
                Objects.equals(carNumber, parking.carNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ParkingLotId, id, carNumber);
    }

    @Override
    public String toString() {
        return "ktvEntity.Parking{" +
                "ParkingLotId='" + ParkingLotId + '\'' +
                ", id=" + id +
                ", carNumber='" + carNumber + '\'' +
                '}';
    }
}
