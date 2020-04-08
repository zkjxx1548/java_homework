package repository;

import jdbc.DbUtil;
import ktvEntity.Parking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParkingRepository {
    public List<Parking> queryAllParking(String parkingLotId) {
        String tableName = "parking_lot_" + parkingLotId.toLowerCase();
        String sql = "SELECT id,car_number FROM " + tableName;
        Connection conn;
        PreparedStatement pt;
        ResultSet rs;
        List<Parking> parkings = new ArrayList<>();
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            rs = pt.executeQuery();
            while (rs.next()) {
                parkings.add(new Parking(
                        parkingLotId,
                        rs.getInt("id"),
                        rs.getString("car_number")
                ));
            }
            DbUtil.closeJDBC(rs, pt, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return parkings;
    }

    public void add(int id, String carNumber, String parkingLotId) {
        String tableName = "parking_lot_" + parkingLotId.toLowerCase();
        String sql = "INSERT INTO " + tableName + "(id,car_number) VALUES (?,?)";
        Connection conn;
        PreparedStatement pt;
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            pt.setInt(1,id);
            pt.setString(2, carNumber);
            pt.executeUpdate();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Parking queryByTicket(String ticket) {
        String[] ticketInfo = ticket.split(",");
        String tableName = "parking_lot_" + ticketInfo[0].toLowerCase();
        String sql = "SELECT id,car_number FROM " + tableName  + " WHERE id = ? AND car_number = ?";
        Connection conn;
        PreparedStatement pt;
        ResultSet rs;
        Parking parking = null;
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            pt.setInt(1, Integer.parseInt(ticketInfo[1]));
            pt.setString(2, ticketInfo[2]);
            rs = pt.executeQuery();
            while (rs.next()) {
                parking = new Parking(ticketInfo[0],
                        rs.getInt("id"),
                        rs.getString("car_number"));
            }
            DbUtil.closeJDBC(rs, pt, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return parking;
    }

    public void deleteByParking(Parking parking) {
        String tableName = "parking_lot_" + parking.getParkingLotId().toLowerCase();
        String sql = "DELETE FROM " + tableName  + " WHERE id = ?";
        Connection conn;
        PreparedStatement pt;
        try {
            conn = DbUtil.getConnection();
            pt = conn.prepareStatement(sql);
            pt.setInt(1, parking.getId());
            pt.executeUpdate();
            pt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
