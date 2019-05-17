package com.example.cinema.blImpl.sales;

import com.example.cinema.bl.management.ScheduleService;
import com.example.cinema.bl.promotion.CouponService;
import com.example.cinema.bl.sales.TicketService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.blImpl.management.schedule.ScheduleServiceForBl;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.promotion.VIPCardMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liying on 2019/4/16.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    CouponService couponService;
    @Autowired
    ScheduleServiceForBl scheduleService;
    @Autowired
    HallServiceForBl hallService;
    @Autowired
    CouponServiceForBl couponServiceForBl;
    @Autowired
    ActivityServiceForBl activityServiceForBl;
    @Autowired
    VIPServiceForBl vipServiceForBl;
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    VIPCardMapper vipCardMapper;

    @Override
    @Transactional
    /** SomeQuestionForaddTicket()
     *  buildSuccess(TicketWithCouponVO)
     */
    public ResponseVO addTicket(TicketForm ticketForm) {
        try{
            Timestamp d = new Timestamp(System.currentTimeMillis());//获取当前时间
            List<SeatForm> seats=ticketForm.getSeats();
            List<Ticket> ticketList=new ArrayList<>();
            for(SeatForm seat : seats){
                //自动生成id（数据库）
                Ticket ticket=new Ticket();
                ticket.setUserId(ticketForm.getUserId());
                ticket.setScheduleId(ticketForm.getScheduleId());
                ticket.setColumnIndex(seat.getColumnIndex());
                ticket.setRowIndex(seat.getRowIndex());
                ticket.setState(0);
                ticket.setTime(d);
                //ticketMapper.cleanExpiredTicket();//（MySQL、timediff）需修改(TicketMapper.xml)
                //应该用在complete里面，所调用为ticket里面的timestamp（time变量），然后比较当前时间now()
                ticketList.add(ticket);
            }
            ticketMapper.insertTickets(ticketList);
            //计算出电影票总价
            double totalPrice=0;
            for(Ticket tempTicket : ticketList){
                int scheduleId=tempTicket.getScheduleId();
                TicketWithScheduleVO ticketWithScheduleVO=tempTicket.getWithScheduleVO();
                ticketWithScheduleVO.setSchedule(scheduleMapper.selectScheduleById(scheduleId));
                ScheduleItem scheduleItem=ticketWithScheduleVO.getSchedule();
                double ticketPrice=scheduleItem.getFare();
                totalPrice+=ticketPrice;
            }
            //选择最优优惠方案
            List<Coupon> couponList= couponServiceForBl.getCouponListByUserId(ticketForm.getUserId());
            if (couponList==null){
                Coupon coupon=new Coupon();
                coupon.setDiscountAmount(0);
                coupon.setTargetAmount(0);
                coupon.setId(0);
            }
            TicketWithCouponVO ticketWithCouponVO=new TicketWithCouponVO();
            ticketWithCouponVO.setCoupons(couponList);
            ticketWithCouponVO.setActivities(activityServiceForBl.getActivitiesByMovie(ticketList.get(0).getId()));
            List<TicketVO> ticketVOList=new ArrayList<>();
            for(Ticket t : ticketList){
                ticketVOList.add(t.getVO());
            }
            ticketWithCouponVO.setTicketVOList(ticketVOList);
            ticketWithCouponVO.setTotal(totalPrice);
            return ResponseVO.buildSuccess(ticketWithCouponVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    @Transactional
    public ResponseVO completeTicket(List<Integer> id, int couponId) {
        try{
            int userId=0;
            int movieId=0;
            for (Integer a:id){
                Ticket ticket=ticketMapper.selectTicketById(a);
                ticketMapper.cleanExpiredTicket();
                userId=ticket.getUserId();
                if (ticket.getState()==0){
                    ticketMapper.updateTicketState(a,1);
                }
                else if (ticket.getState()==2){
                    return ResponseVO.buildFailure("失败");
                }
                int scheduleId=ticket.getScheduleId();
                TicketWithScheduleVO ticketWithScheduleVO=ticket.getWithScheduleVO();
                ticketWithScheduleVO.setSchedule(scheduleMapper.selectScheduleById(scheduleId));
                ScheduleItem scheduleItem=ticketWithScheduleVO.getSchedule();
                movieId=scheduleItem.getMovieId();
            }
            if (couponId!=0) {
                couponServiceForBl.deleteCouponUser(couponId, userId);
            }
            List<Activity> activities=activityServiceForBl.getActivitiesByMovie(movieId);
            if (activities!=null) {
                for (Activity activity : activities) {
                    Timestamp d = new Timestamp(System.currentTimeMillis());
                    Timestamp startTime = activity.getStartTime();
                    Timestamp endTime = activity.getEndTime();
                    if (d.before(endTime) && d.after(startTime)) {
                        Coupon coupon=activity.getCoupon();
                        couponService.issueCoupon(coupon.getId(),userId);
                    }
                }
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    public ResponseVO getBySchedule(int scheduleId) {
        try {
            List<Ticket> tickets = ticketMapper.selectTicketsBySchedule(scheduleId);
            ScheduleItem schedule=scheduleService.getScheduleItemById(scheduleId);
            Hall hall=hallService.getHallById(schedule.getHallId());
            int[][] seats=new int[hall.getRow()][hall.getColumn()];
            tickets.stream().forEach(ticket -> {
                seats[ticket.getRowIndex()][ticket.getColumnIndex()]=1;
            });
            ScheduleWithSeatVO scheduleWithSeatVO=new ScheduleWithSeatVO();
            scheduleWithSeatVO.setScheduleItem(schedule);
            scheduleWithSeatVO.setSeats(seats);
            return ResponseVO.buildSuccess(scheduleWithSeatVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTicketByUser(int userId) {
        try{
            List<Ticket> listTicket=ticketMapper.selectTicketByUser(userId);
            List<TicketWithScheduleVO> listTicketWithScheduleVO=new ArrayList<>();
            for(Ticket ticket : listTicket){
                TicketVO ticketVO=ticket.getVO();
                TicketWithScheduleVO temp=new TicketWithScheduleVO();
                temp.setId(ticketVO.getId());
                temp.setUserId(ticketVO.getUserId());
                ScheduleItem scheduleItem=scheduleService.getScheduleItemById(ticketVO.getScheduleId());
                temp.setSchedule(scheduleItem);
                temp.setColumnIndex(ticketVO.getColumnIndex());
                temp.setRowIndex(ticketVO.getRowIndex());
                temp.setState(ticketVO.getState());
                temp.setTime(ticketVO.getTime());
                if(!temp.getState().equals("未完成")) {
                    listTicketWithScheduleVO.add(temp);
                }
            }
            return ResponseVO.buildSuccess(listTicketWithScheduleVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }

    @Override
    @Transactional
    public ResponseVO completeByVIPCard(List<Integer> id, int couponId) {
        try {
            double discount=0;

            int userId = 0;
            int movieId = 0;
            for (Integer a : id) {
                Ticket ticket = ticketMapper.selectTicketById(a);
                ticketMapper.cleanExpiredTicket();
                userId = ticket.getUserId();
                if (ticket.getState() == 0) {
                    ticketMapper.updateTicketState(a, 1);
                } else if (ticket.getState() == 2) {
                    return ResponseVO.buildFailure("失败");
                }
                int scheduleId = ticket.getScheduleId();
                TicketWithScheduleVO ticketWithScheduleVO = ticket.getWithScheduleVO();
                ticketWithScheduleVO.setSchedule(scheduleMapper.selectScheduleById(scheduleId));
                ScheduleItem scheduleItem = ticketWithScheduleVO.getSchedule();
                movieId = scheduleItem.getMovieId();
            }
            if (couponId != 0) {
                couponServiceForBl.deleteCouponUser(couponId, userId);
                Coupon coupon = couponServiceForBl.getCouponById(couponId);
                discount = coupon.getDiscountAmount();
            }
            List<Activity> activities = activityServiceForBl.getActivitiesByMovie(movieId);
            if (activities != null) {
                for (Activity activity : activities) {
                    Timestamp d = new Timestamp(System.currentTimeMillis());
                    Timestamp startTime = activity.getStartTime();
                    Timestamp endTime = activity.getEndTime();
                    if (d.before(endTime) && d.after(startTime)) {
                        Coupon coupon = activity.getCoupon();
                        couponService.issueCoupon(coupon.getId(), userId);
                    }
                }
            }

            //获取会员卡信息

            Ticket temp = ticketMapper.selectTicketById(id.get(0));
            int UserId = temp.getUserId();
            VIPCard vipCard = vipServiceForBl.getVIPCardByUserId(UserId);
            if (vipCard == null) {
                return ResponseVO.buildFailure("VIP卡不存在");
            } else {
                //更新会员卡信息
                double newBalance = 0;
                //计算电影票总价
                int scheduleId = temp.getScheduleId();
                TicketWithScheduleVO ticketWithScheduleVO = temp.getWithScheduleVO();
                ticketWithScheduleVO.setSchedule(scheduleMapper.selectScheduleById(scheduleId));
                ScheduleItem scheduleItem = ticketWithScheduleVO.getSchedule();
                double ticketPrice = scheduleItem.getFare();
                ticketPrice = ticketPrice * id.size();
                newBalance = vipCard.getBalance() - (ticketPrice - discount);
                if (newBalance < 0) {
                    for(Integer a :id) {
                        ticketMapper.updateTicketState(a, 0);
                    }
                    return ResponseVO.buildFailure("余额不足");
                } else {
                    vipCard.setBalance(newBalance);
                    vipCardMapper.updateCardBalance(vipCard.getId(), newBalance);
                    VIPCardVO vipCardVO = vipCard.toVO();
                    return ResponseVO.buildSuccess(vipCardVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO cancelTicket(List<Integer> id) {
        try{
            for (Integer a:id){
                Ticket ticket=ticketMapper.selectTicketById(a);
                if (ticket!=null){
                    ticketMapper.deleteTicket(a);
                }
            }
            return ResponseVO.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }

    }



}
