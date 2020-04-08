package repository;

import jdbc.DbUtil;
import ktvEntity.ParkingLot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotRepository {
    public void deleteHistory() {
        String sql = "DELETE FROM parking_lot";
        Connection conn;
        PreparedStatement pt;
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            pt.executeUpdate();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ParkingLot parkingLot) {
        String sql = "INSERT INTO parking_lot(id, car_count) VALUES (?,?)";
        Connection conn;
        PreparedStatement pt;
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            pt.setString(1, parkingLot.getId());
            pt.setInt(2, parkingLot.getCarCount());
            pt.executeUpdate();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createParkingLot_XTable(ParkingLot parkingLot) {
        String tableName = "parking_lot_" + parkingLot.getId().toLowerCase();
        String sqlDrop = "DROP TABLE IF EXISTS " + tableName;
        String sql = "CREATE TABLE " + tableName + " (\n" +
                "  id INT NOT NULL,\n" +
                "  car_number VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (id))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8\n" +
                "COLLATE = utf8_bin";
        Connection conn;
        PreparedStatement pt;
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sqlDrop);
            pt.executeUpdate();
            pt = conn.prepareStatement(sql);
            pt.executeUpdate();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ParkingLot> queryAllParkingLot() {
        String sql = "SELECT id,car_count FROM parking_lot";
        Connection conn;
        PreparedStatement pt;
        ResultSet rs;
        List<ParkingLot> parkingLots = new ArrayList<>();
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            rs = pt.executeQuery();
            while (rs.next()) {
                parkingLots.add(new ParkingLot(
                        rs.getString("id"),
                        rs.getInt("car_count")
                ));
            }
            DbUtil.closeJDBC(rs, pt, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return parkingLots;
    }
}
