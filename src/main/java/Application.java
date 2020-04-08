import exception.InvalidTicketException;
import exception.ParkingLotFullException;
import ktvEntity.Parking;
import ktvEntity.ParkingLot;
import repository.ParkingLotRepository;
import repository.ParkingRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {

  public static void main(String[] args) {
    operateParking();
  }

  public static void operateParking() {
    while (true) {
      System.out.println("1. 初始化停车场数据\n2. 停车\n3. 取车\n4. 退出\n请输入你的选择(1~4)：");
      Scanner printItem = new Scanner(System.in);
      String choice = printItem.next();
      if (choice.equals("4")) {
        System.out.println("系统已退出");
        break;
      }
      handle(choice);
    }
  }

  private static void handle(String choice) {
    Scanner scanner = new Scanner(System.in);
    switch (choice) {
      case "1":
        System.out.println("请输入初始化数据\n格式为\"停车场编号1：车位数,停车场编号2：车位数\" 如 \"A:8,B:9\"：");
        String initInfo = scanner.next();
        init(initInfo);
        break;
      case "2": {
        System.out.println("请输入车牌号\n格式为\"车牌号\" 如: \"A12098\"：");
        String carInfo = scanner.next();
        String ticket = park(carInfo);
        String[] ticketDetails = ticket.split(",");
        System.out.format("已将您的车牌号为%s的车辆停到%s停车场%s号车位，停车券为：%s，请您妥善保存。\n", ticketDetails[2], ticketDetails[0], ticketDetails[1], ticket);
        break;
      }
      case "3": {
        System.out.println("请输入停车券信息\n格式为\"停车场编号1,车位编号,车牌号\" 如 \"A,1,8\"：");
        String ticket = scanner.next();
        String car = fetch(ticket);
        System.out.format("已为您取到车牌号为%s的车辆，很高兴为您服务，祝您生活愉快!\n", car);
        break;
      }
    }
  }

  public static void init(String initInfo) {
    String[] parkingLotsInfos = initInfo.split(",");
    ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
    parkingLotRepository.deleteHistory();
    Arrays.stream(parkingLotsInfos)
            .map(s -> {
              String[] plInfos = s.split(":");
              return new ParkingLot(plInfos[0], Integer.parseInt(plInfos[1]));
            })
            .forEach(parkingLot -> {
                    parkingLotRepository.save(parkingLot);
                    parkingLotRepository.createParkingLot_XTable(parkingLot);
            });
  }

  public static String park(String carNumber) {
    ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
    ParkingRepository parkingRepository = new ParkingRepository();
    List<ParkingLot> parkingLots = parkingLotRepository.queryAllParkingLot();
    AtomicInteger carId = new AtomicInteger();
    String s = parkingLots.stream()
            .sorted(Comparator.comparingInt(ParkingLot::getIdASCII))
            .filter(parkingLot -> {
              List<Parking> parkings = parkingRepository.queryAllParking(parkingLot.getId());
              if (parkingLot.getCarCount() != parkings.size()) {
                carId.set(Application.getParkingId(parkings));
              }
              return parkingLot.getCarCount() != parkings.size();
            })
            .findFirst()
            .map(parkingLot -> {
              parkingRepository.add(Integer.parseInt(carId.toString()), carNumber, parkingLot.getId());
              return String.format("%s,%s,%s", parkingLot.getId(), carId.toString(), carNumber);
            })
            .orElse("ParkingLotFull");
    if (Objects.equals(s, "ParkingLotFull")) {
      throw new ParkingLotFullException();
    }
    return s;
  }

  public static String fetch(String ticket) {
    ParkingRepository parkingRepository = new ParkingRepository();
    Parking parking = parkingRepository.queryByTicket(ticket);
    if (parking != null) {
      parkingRepository.deleteByParking(parking);
    } else {
      throw new InvalidTicketException();
    }
    return parking.getCarNumber();
  }

  public static int getParkingId(List<Parking> parkings) {
    final int[] id = {1};
    return parkings.stream()
            .filter(parking -> {
              if (parking.getId() == id[0]) {
                id[0]++;
              }
              return parking.getId() == id[0] - 1;
            }).max(Comparator.comparingInt(Parking::getId))
            .map(parking -> parking.getId() + 1)
            .orElse(1);
  }
}
