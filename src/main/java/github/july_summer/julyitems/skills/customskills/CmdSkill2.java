package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CmdSkill extends SkillCooldown implements SkillExecute,SkillCustomLore {

    public CmdSkill(){
        super("cmd2");
        SkillManager.registerCommand(CmdSkill.class);
        SkillManager.registerCustomLore("cmd2", this);
    }

    @Override
    public boolean isDisplayLore() {
        return false;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return null;
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(1));
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        boolean isOp = p.isOp();
        p.setOp(Util.objectToBoolean(data.getData(0)));
        p.chat("/" + data.getData(2).toString().replace("{player}", p.getName()));
        p.setOp(isOp);
    }

    @SubCommand(
            cmd = "addSkill cmd2 <触发方式> <是否OP权限执行> <冷却> <命令>",
            msg = "添加执行命令 true为是 false为否 {player}为玩家变量",
            checkArgs1 = 2,
            checkArgs2 = 1,
            checkLength =  false
    )
    public static void cmdSkill(CommandSender sender, String[] args){
        if(args.length < 6){
            sender.sendMessage("§c参数错误");
            return;
        }
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!(args[4].equalsIgnoreCase("true") || args[4].equalsIgnoreCase("false"))){
            sender.sendMessage("§c请填写true or false");
            return;
        }
        if(!Util.isNumber(args[5])){
            sender.sendMessage("§c冷却请输入整数");
            return;
        }
        String command = Util.argsToString(args).split(" ", 7)[6];
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4], Integer.parseInt(args[5]), command}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("cmd2") + " 添加成功");
    }

}
