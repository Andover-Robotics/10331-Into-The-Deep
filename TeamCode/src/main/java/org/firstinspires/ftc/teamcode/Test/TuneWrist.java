package org.firstinspires.ftc.teamcode.Test;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Bot;
@TeleOp(name = "TuneClawAndWrist", group = "Test")
public class TuneWrist extends LinearOpMode {
    Bot bot;
    private GamepadEx gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = Bot.getInstance(this);
        gp2 = new GamepadEx(gamepad2);
        waitForStart();
        double pos=0;

        while (opModeIsActive() && !isStopRequested()) {
            gp2.readButtons();
            telemetry.addData("Diffy 1 Current Position", bot.wrist.diffy1.getPosition());
            telemetry.addData("Diffy 2 Current Position", bot.wrist.diffy2.getPosition());
            //telemetry.addData("Claw Current Position", bot.claw.claw.getPosition());


            telemetry.update();
            if(gp2.wasJustPressed(GamepadKeys.Button.A)){
                if(pos>0){
                    pos=pos-0.01;
                }
                bot.wrist.move(false,pos);
            }


            if(gp2.wasJustPressed(GamepadKeys.Button.B)){
                if(pos<1){
                    pos=pos+0.01;
                }
                bot.wrist.move(true,pos);
            }
        }
    }
}