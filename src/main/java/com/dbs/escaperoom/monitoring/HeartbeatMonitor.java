package com.dbs.escaperoom.monitoring;

import com.dbs.escaperoom.models.SuccessMessage;
import com.dbs.escaperoom.repositories.RoomRegistrationRepository;
import com.dbs.escaperoom.models.RoomRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Daan on 17-Oct-17.
 */
@Component
public class HeartbeatMonitor {

    @Autowired
    private RoomRegistrationRepository roomRepository;

    protected int intervalSecond;
    private Timer heartbeatTimer;
    public HeartbeatMonitor(){
        intervalSecond = 5;
        heartbeatTimer = new Timer();
    }

    public void start(){
        try {
            SSLUtil.turnOffSslChecking();
        }
        catch(Exception ex){
            System.out.println("boeh");
        }
        System.out.println(roomRepository);
        heartbeatTimer.schedule(new HeartbeatTask(roomRepository,intervalSecond),1000,1000);
    }

    class HeartbeatTask extends TimerTask {

        private RoomRegistrationRepository roomRegistrationRepository;
        private int intervalSecond;

        public HeartbeatTask(RoomRegistrationRepository roomRegistrationRepository,
                             int intervalSecond){
            this.intervalSecond = intervalSecond;
            this.roomRegistrationRepository = roomRegistrationRepository;


        }

        public void run(){
            Date date = new Date();
            date.setTime(date.getTime()-(this.intervalSecond*10));
            List<RoomRegistration> regs = this.roomRegistrationRepository.forceWhereHeratbeatDue(date);
            for(RoomRegistration rr : regs){
                System.out.println(rr.getName());
                this.sendHeartBeat(rr);
            }
        }
        @Async
        void sendHeartBeat(RoomRegistration rr){
            try {
                RestTemplate restTemplate = new RestTemplate();
                SuccessMessage quote = restTemplate.getForObject(rr.getEndPoint()+"/registration/heartbeat", SuccessMessage.class);
                if(quote.getMessage().equals("success")) {
                    rr.setOnline(true);
                }
                else{
                    rr.setOnline(false);
                }
                rr.setLastHeartbeat(new Date());
                roomRegistrationRepository.save(rr);
            }
            catch(Exception ex){
                //Heartbeat didn't work set registration to be offline
                //But still ste the heartbeat to have beaten
                //TODO: remove from database after too many failed beats.
                rr.setLastHeartbeat(new Date());
                rr.setOnline(false);
                roomRegistrationRepository.save(rr);
            }
        }
    }
}
