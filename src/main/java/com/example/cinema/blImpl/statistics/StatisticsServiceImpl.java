package com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.blImpl.management.hall.HallServiceForBl;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private HallServiceForBl hallServiceForBl;
    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public ResponseVO getScheduleRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));

            Date nextDate = getNumDayAfterDate(requireDate, 1);
            return ResponseVO.buildSuccess(movieScheduleTimeList2MovieScheduleTimeVOList(statisticsMapper.selectMovieScheduleTimes(requireDate, nextDate)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTotalBoxOffice() {
        try {
            return ResponseVO.buildSuccess(movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(statisticsMapper.selectMovieTotalBoxOffice()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getAudiencePriceSevenDays() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -6);
            List<AudiencePriceVO> audiencePriceVOList = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                AudiencePriceVO audiencePriceVO = new AudiencePriceVO();
                Date date = getNumDayAfterDate(startDate, i);
                audiencePriceVO.setDate(date);
                List<AudiencePrice> audiencePriceList = statisticsMapper.selectAudiencePrice(date, getNumDayAfterDate(date, 1));
                double totalPrice = audiencePriceList.stream().mapToDouble(item -> item.getTotalPrice()).sum();
                audiencePriceVO.setPrice(Double.parseDouble(String.format("%.2f", audiencePriceList.size() == 0 ? 0 : totalPrice / audiencePriceList.size())));
                audiencePriceVOList.add(audiencePriceVO);
            }
            return ResponseVO.buildSuccess(audiencePriceVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getMoviePlacingRateByDate(Date date) {
        try {
            List<PlacingRateVO> placingRateVOList=new ArrayList<>();
            List<MovieScheduleTime> movieScheduleTimeList=statisticsMapper.selectMovieScheduleTimes(date,getNumDayAfterDate(date,1));
            for(MovieScheduleTime movieScheduleTime:movieScheduleTimeList){
                double seatNum=0;
                double AudienceNum=0;
                int scheduleTime=movieScheduleTime.getTime();
                List<ScheduleItem> scheduleItemList=scheduleMapper.selectScheduleByMovieIdAndDate(movieScheduleTime.getMovieId(),date,getNumDayAfterDate(date,1));
                for (ScheduleItem scheduleItem:scheduleItemList){
                    Hall hall=hallServiceForBl.getHallById(scheduleItem.getHallId());
                    int row=hall.getRow();
                    int column=hall.getColumn();
                    seatNum=seatNum+row*column;
                    List<Ticket> tickets=ticketMapper.selectTicketsBySchedule(scheduleItem.getId());
                    for (Ticket ticket:tickets){
                        if (ticket.getState()==1){
                            AudienceNum++;
                        }
                    }
                }
                double placingRate=0;
                placingRate=AudienceNum/scheduleTime/seatNum;
                PlacingRateVO placingRateVO=new PlacingRateVO();
                placingRateVO.setId(movieScheduleTime.getMovieId());
                placingRateVO.setName(movieScheduleTime.getName());
                placingRateVO.setPlacingRate(placingRate);
                placingRateVOList.add(placingRateVO);
            }
            return ResponseVO.buildSuccess(placingRateVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        try {
            //获取一个最近days内所有电影的总票房List<MovieBoxOfficeVO>（VO包括movieId,boxOffice,name?）
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, 1 - days);
            List<MovieBoxOffice>  movieBoxOfficeList =statisticsMapper.selectMovieBoxOfficeByDates(startDate,today);
            List<MovieBoxOfficeVO> movieBoxOfficeVOList=new ArrayList<>();
            int times=0;
            for(MovieBoxOffice m : movieBoxOfficeList){
                if(times<movieNum){
                    times++;
                    movieBoxOfficeVOList.add(m.toVO());
                }
                else{
                    break;
                }
            }
            return ResponseVO.buildSuccess(movieBoxOfficeVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }


    /**
     * 获得num天后的日期
     * @param oldDate
     * @param num
     * @return
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }

    private List<MovieScheduleTimeVO> movieScheduleTimeList2MovieScheduleTimeVOList(List<MovieScheduleTime> movieScheduleTimeList){
        List<MovieScheduleTimeVO> movieScheduleTimeVOList = new ArrayList<>();
        for(MovieScheduleTime movieScheduleTime : movieScheduleTimeList){
            movieScheduleTimeVOList.add(new MovieScheduleTimeVO(movieScheduleTime));
        }
        return movieScheduleTimeVOList;
    }


    private List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(List<MovieTotalBoxOffice> movieTotalBoxOfficeList){
        List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeVOList = new ArrayList<>();
        for(MovieTotalBoxOffice movieTotalBoxOffice : movieTotalBoxOfficeList){
            movieTotalBoxOfficeVOList.add(new MovieTotalBoxOfficeVO(movieTotalBoxOffice));
        }
        return movieTotalBoxOfficeVOList;
    }

}
