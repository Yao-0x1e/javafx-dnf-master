package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;
import com.garena.dnfmaster.constant.ExpertJob;
import com.garena.dnfmaster.constant.GrowType;
import com.garena.dnfmaster.constant.PvpGrade;
import com.garena.dnfmaster.mapper.*;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.util.ItemUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CharacService {
    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private CharacQuestShopMapper characQuestShopMapper;
    @Autowired
    private PvpResultMapper pvpResultMapper;
    @Autowired
    private CharacInfoMapper characInfoMapper;
    @Autowired
    private GuildMemberMapper guildMemberMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private CreatureItemMapper creatureItemMapper;
    @Autowired
    private UserItemMapper userItemMapper;
    @Autowired
    private NewCharacRequestMapper newCharacRequestMapper;
    @Autowired
    private CharacTitleBookMapper characTitleBookMapper;
    @Autowired
    private CharacManageInfoMapper characManageInfoMapper;
    @Autowired
    private CharacStatMapper characStatMapper;

    public void setSP(List<Charac> characters, String inputSP) {
        int sp = Integer.parseInt(inputSP);
        Assert.isTrue(sp >= 0, "请确保输入的SP数值为非负整数");
        for (Charac character : characters) {
            skillMapper.setSP(character.getNo(), sp);
        }
    }

    public void setTP(List<Charac> characters, String inputTP) {
        int tp = Integer.parseInt(inputTP);
        Assert.isTrue(tp >= 0, "请确保输入的TP数值为非负整数");
        for (Charac character : characters) {
            skillMapper.setTP(character.getNo(), tp);
        }
    }

    public void setQP(List<Charac> characters, String inputQP) {
        int qp = Integer.parseInt(inputQP);
        Assert.isTrue(qp >= 0, "请确保输入的QP数值为非负整数");
        for (Charac character : characters) {
            characQuestShopMapper.setQP(character.getNo(), qp);
        }
    }

    public void setPvpGrade(List<Charac> characters, String inputPvpGrade) {
        int pvpGrade = Integer.parseInt(inputPvpGrade);
        Assert.checkBetween(pvpGrade, PvpGrade.MIN_VALUE, PvpGrade.MAX_VALUE, "段位不在合法区间内");
        for (Charac character : characters) {
            pvpResultMapper.setPvpGrade(character.getNo(), pvpGrade);
        }
    }

    public void setPvpPoint(List<Charac> characters, String inputPvpPoint) {
        int pvpPoint = Integer.parseInt(inputPvpPoint);
        Assert.isTrue(pvpPoint >= 0, "请确保输入的胜点数值为非负整数");
        for (Charac character : characters) {
            pvpResultMapper.setPvpPoint(character.getNo(), pvpPoint);
        }
    }

    public void setPvpWin(List<Charac> characters, String inputPvpWin) {
        int win = Integer.parseInt(inputPvpWin);
        Assert.isTrue(win >= 0, "请确保输入的胜场数值为非负整数");
        for (Charac character : characters) {
            pvpResultMapper.setPvpWin(character.getNo(), win);
        }
    }

    public void setPvpLose(List<Charac> characters, String inputPvpLose) {
        int lose = Integer.parseInt(inputPvpLose);
        Assert.isTrue(lose >= 0, "请确保输入的败场数值为非负整数");
        for (Charac character : characters) {
            pvpResultMapper.setPvpLose(character.getNo(), lose);
        }
    }

    public void setGrowType(List<Charac> characters, int growType) {
        Assert.isTrue((growType >= GrowType.MIN_JOB_VALUE && growType <= GrowType.MAX_JOB_VALUE) || (growType >= GrowType.MIN_AWAKE_VALUE && growType <= GrowType.MAX_AWAKE_VALUE), "主职业的数值不在合法区间内");
        for (Charac character : characters) {
            setGrowType(character.getNo(), growType);
        }
    }

    private void setGrowType(int characNo, int growType) {
        int job = characInfoMapper.findJob(characNo);
        Assert.isFalse(job == 9 || job == 10, "黑暗骑士和缔造者不支持转职或觉醒");
        Assert.isFalse((job == 6 || job == 8) && (growType == 3 || growType == 4 || growType == 19 || growType == 20), "男法师和盗贼只有两种职业或觉醒");

        characInfoMapper.setGrowType(characNo, growType);
        guildMemberMapper.setGrowType(characNo, growType);
    }

    public void setExpertJob(List<Charac> characters, int expertJob) {
        Assert.isTrue(expertJob >= ExpertJob.MIN_VALUE && expertJob <= ExpertJob.MAX_VALUE, "副职业的数值不在合法区间内");
        for (Charac character : characters) {
            characInfoMapper.setExpertJob(character.getNo(), expertJob);
        }
    }

    public void setMaxExpertJobLevel(List<Charac> characters) {
        for (Charac character : characters) {
            Integer expertJob = characInfoMapper.findExpertJob(character.getNo());
            if (expertJob != null && expertJob != 0) {
                characStatMapper.setMaxExpertJobLevel(character.getNo());
            }
        }
    }

    public void setMaxInvenWeight(List<Charac> characters) {
        for (Charac character : characters) {
            characInfoMapper.setMaxInvenWeight(character.getNo());
        }
    }

    public void clearInven(List<Charac> characters) {
        for (Charac character : characters) {
            inventoryMapper.clearInven(character.getNo());
        }

    }

    public void clearCreatures(List<Charac> characters) {
        for (Charac character : characters) {
            creatureItemMapper.clearCreatures(character.getNo());
        }
    }

    public void clearAvatas(List<Charac> characters) {
        for (Charac character : characters) {
            userItemMapper.clearAvatas(character.getNo());
        }
    }

    public void clearQuests(List<Charac> characters) {
        for (Charac character : characters) {
            newCharacRequestMapper.clearQuests(character.getNo());
        }
    }

    public void clearAllQuests(List<Charac> characters) {
        String[] clearQuests = new String[]{
                "30750000789CEDD8DB72C220100050F8FF9FEE8C6D080BA492943A193DE7C10B8605172460CA4FA5E797944B83EFAA8F174DBC14DFE6BACE16A92ACDE1696C8F72D09BABF24CEBE3FEE4D89F52D25F1AF21CCB5395B4AD17B9CBE9D9BE9DAA548D5A18AD4519FE69E3B7C6993297AB9CE2D05D4CEF41B5970FD6B8C1BEF4E0D7B7AAC1A566A77C1E5D7BA2EEB0E1C92934158FCA234356B37BDB762C8FD7479FDF4CD95D6CBB84951BA0D24879CA4DD172AF5861F7873B8E28B4FE6BA2F66BC51BFC209A33554EE5F4B22E8DF10096FB9DF5D226264EE26D855C8294437713B969A40B70D5A23021647F9EAE3EDBA671D78F7D7EEF27F95C1ED351CE8603D005CF718A0DEB9C3CC13E4FC376D54C8EAB716E4B463D879AF9F171AA656B41946602CD940EE7DB60FDDB3E296FEB45AFFE2F2C866EB73A717D1C7E9774F1D4D77CA370FBBA100EE04359386FA0B90FFF65388C2400EFCA3D0E000000000000000000000000000000000000002EF802C33903C7",
                "30750000789CEDD8D192C2200C4051F2FF3FBD33B5A409499532D1E9EA3D2F6E91062494C23679A9BDAEA2559DC7ADDB1F43BCE62FC5DED3239952711FB923CA496F56C94CEB797FC4F7474B625537CEBEBC9941EBBD9030A657FB262E3313F57B3625E6A5C46927EA327963453F722E8C349FBAC5964F6EFB78B2F20663E9C9D357D560A9D9D92059DD0BF7A60D4F4EA1A978301E4B2E03756BFBE6634B523ACD6F983EDD5DF45D42E506481BD10F198ACA7D628835C1BFB0BFC01778D7448D6BC5173C10C3996A5FD3C379B3A00D3D4BC59D7569131327F1F106D1207AE81E220F8D8400AB8AC2B890F13C6DBEEBD338F4E398DF5A71DFE54ADF3DC758270908C17D8BF93D170F774FC7404C4AA7C6D8E4792C710F0790607EFC1CB36C154479BE32E695B38693F5AF7FA39776D1B3FF0BF3A1C7AD8E5F1FD3DFD2164F7DC32F72AFAF857000F0A358382F38DE76B54366DEA3DB652F5E0975B11C0080FF82771900000000000000000000000000000000000000000000000000000000000BFE00522603E7",
                "30750000789CEDD8D972C220140050F8FF9FEE8C3584552183B68EE73CD44AE05E044296109F0ACFABA4AA85DFA6B77FAA78A1FC1AF33647A4AC34161F7D6794416FAE8A33D9FBFD89657F52495BB518E7B23C648376F4223663BADAB7A6D1A340D9AC15B3B56984EF39860776CDE4C759FEE9730D6228A7EEE2F00E9ABD7DB2066BB653B26B47D813E6618AC91CB15777A16D37F1E4129A8A47E6B602E70698BF92DF937497F93F9CBD747771DC25ECBC014A49D247AC8AB67BCB101F33FCBDF7177C94572DD4CEC3C04BF2BC55F54C753FCF9BE7CD0D398EE18BED9DF5D614134FE275839882A487EE2A7295A40970D5A63045C8F6793A3B762CE3A61FF9817338CEBF8331EB4E4013BCC898A6BE57EBDA8C0F7E6AA8323FAA9D5AD525C5C9011DD6C7D7C9B6AD0D51AA053453DA5D6F9DFDEF3892BEE69B5E28F7FBACF9F0BDE770A5775FB22C0E42EAA9530A60958D73C179B5DB3B64E535367B13B89EE4490B130DC0C7721103000000000000000000000000000000000000800B7E00149903E9",
                "30750000789CEDD9DB72C2201400C09CFFFFE9CE54416E51C2D08EB5BB7D8812385040027AC44BC7EB2C396BE556F4FB4513EFA8DF465926452A52A3BA8C3DA29CB46655CCD43E6E4FD4EDC9297DD6AA9FEBF4A3E8B4D48AE8FAF46ADB9E14EAEF16A3568DD6A61EBED7F1A4391BEBF900E73D32D75771D443B7D8BD676D588BB66E5C619F7AF2E9DB55E156B3533E46792F941D563C3985A6E25188340975D5FB8A78ACA3C369FE86839777176997B07303942BC9976892B6FB9D2EBE0FB1FD057FC24F4DD47EADF8800F4473A68A239F5EF675637D008B7E67BDB58A8993785B2072907CE86E2237957401566D0A5385ECCFD3C5BD348DBB763CE677FA56218ABFDBA58B7532005DF068A6D8A8CCC513ECEB6E48B966FAB818E736A56A390C981FFF4EB16C6D88D24CA099D4E17C1BAC7FE94E7E5B2E7AE5776175E876AB53AF8FC3FFE5583CF5D5D5D60FDB857000FF9485F30D948FE1F6A782ABBF426ECD0600EFC3C30B00000000000000000000000000000000000000167C01520903DE",
                "30750000789CEDD9DB8EC220100050F8FF9FDEC406E4564582A6BAE7BCA8588611908286F854787E49BEB47254BD3D69E285FA652CEBA4484569AC1EC6EE514EB25915675A1FE713EB7C72497F69D5CF7579283A2D6511BB3E7D35B7A94A65626934633F2E5B9CA6B36F247FDF5C5FC5500FDD62F79E54FBF8608D1BEC4B4FBE7DBB1ADC6A76CAC7D1B52FD41D363C3985A6E251B88F96BEBAACB463B93D1F2D23171CBBBCBB48BB849D1BA0DC487E884DD1761FEAE2638CED2FF80AEF9AA8FD5AF1035F88E64C15433EBDECEBC6FA0016FB9DF5D626264EE26D859883E4437713B969A40BB06A53982A647F9E2EDE4BD3B8CBE33EBFF36547E13160DD51BAFB11E34146758B619CE18B7798C79D500CE9541F17E3DC96D49943EF0BE6C7D5F3FB36C5B2B5214A3381664A87E33958FFD23BF965B9E895F3A20EDD6E75EAF571F859C2E2A9AFF944D5CD61211CC03F65E1BC80E68636FA97653AD46A0A8BF500E053DCAB0000000000000000000000000000000000000060C11FF3EE03DB",
                "30750000789CEDD7DBB283200C4051F2FF3F7D665A8924040B0CB5B467AF875E5002124449F2527A7D8A9E6A3CAB3E7EB878C9FE95B24E8E54948AF98A9D511ABD99253DADC7FD11DB1F2DA94F35E36CCB533168B917528DE968DF862A155933D91A6BF6BAD58B03AB32F9FBFAC64A924DDDE4F036AADD9EACB8C1BAB471F7AD6A70A9DE292FD1B90375C3863BA750573C14E4F8987BA2E0168FA7F4B18E86D37CC3B4E9DB457E4B58F902A48DE897B8A2E56E1962BD1B77CC28E0BD6BA2D66BC50FDC106E4F75DCE7D57E73411B79F8A47EB35EDA44C74EDC57100DA29B6E17D935520598B5288C0959EFA78B63791A57FD38E7F7B99317FD4CAD310B135005173BC5F24325386B2EE38D4B4DAEE5ABB3B5962F713DC747ED9987EF9B1F5FD6DDFD14CBD682286E02F59486090CD6BF7C44FF968B5E39116C68FFAA63D7C7F05AD2E4AECF5D91797C4D8403807F8A857303F6195B6C76C7F3B220934C0600C0967840010000000000000000000000000000000000000000000000000000000030E10FE4BE03DB",
                "30750000789CEDD8DB9283200C80E1E4FD5F7A67BA4B20075B64B0EBB4FF77612D4240C0038ABE24AFB35856E7B7E86327C413FF57C7322DD290AAEEA7D6A31CB46695CED45EB7477D7B2C256775FDECD365E8B4D60A4D7D7AB66DA70A0DA3E6466BBD87F3CC396CCEBE91FC7C737DA5E2876EB17B0F8ABD7DB0EA0A73EAC1D5B7ABC2AD66A7BC56794F942D2B9E9C4253F13050DBFCFDA7CBEEA7BDB13CF66D138EDF8CBD5DB4B7849D2F405689FD6848DAEEAD5D7C415701FB5D354DF305F00197435853A9D8EA655F37FA0598E637EBAD554CACC46301B520B6E80E91432529C0AA4D615CC8BC9E1E8EB5699CDAD1E7775FC9AB6DA5FC3ED1833E3D0FB1CD90BF8EE746747EC40F4E5542CDCF725BA998125B0E44CC8FAF33DCB6CA63E7A2840934935AD61133F44CEDDEEF3EFCEAD8D658B20A2CFD3191EB96C5555F3823F740590807005F8A1BE70DF84774391AB32374CDE7490000FE1F0F25000000000000000000000000000000000000000000000000000000000016FC00962E03F0",
                "30750000789CEDD8DB728320140040F8FF9FEE4CA2C8CD0419E2B4CDEE83A9040E0804A121BE15DE6749590BCFA28F3FAA78A1BC8D79993D52961A8B8FBE23CA496B66C591DAFBED89657B524A9BB5E8E7323D649DB6B722367D7AB56D970A65A3568CD6A21EDEEA78553943C6FA2A8672E826BBF7A4D8ED83D5AFB04D3DF9F5ADAA70A9D1291F7B792F94ED563C388586E29189CF8B8EFACDB6CDC73656BDEF6F6ED080B4BBD877092B3740A992F411ABA4E5EEE8E234C0F617FC099F9AA8ED5AF10F7E10D5996A5BD39BF3E6823AF6EE8BEDCE7A69150327F1BA404C41D2A1BB8A5C55D20498B5284C11B23D4F67DFEDD3B869C731BF8F937C4CD770D667DD016882C7728A6D017BB9E646FCE4514355F3ABDCA9549DD2B41C2AE6C7D7C996AD0551AA093492DA9D6F9DF56FFF26DDE68B5EFEBFB03274BDD529D7C7EEB384C9535FF544C5EB6B221CC097B27076BD7B5FAEEDB2EC3DFAB80DC7F572A875ADBA2930008CF12A0200000000000000000000000000000000000080093FB6CB03DB",
                "30750000789CEDD8096EC320100040EFFF3F5DA9896D16707D944439662A350D310B850D064FB16BDABF64B934B955FDFDA38A37E5B751D6992315A5915EFAD6281BBDB92A8EB4DEEF4FE4FE2C25EDA5699C73F9540CDADC8B68C6F46CDF4E552A662DCDD6A011BEB7F157E31C726CAC62CA5377717837AA3D7DB2FA0DB6A51BDFBE510D0E7534E5A377ED89BADD860FA6D0A178EF62EC3D63AB91F9D75B0FD5878B58D7D16E9ABFE0E42DBB8B7997F088645EF74151150DF79C21BE4FB1FD056FE15189DAAE151FF085A8CE54312DA79771C3980F60F1887B7B3EE5C58EBAC27A005D0EDD55D4F276D10B70D5A03029647B9E2E3E9BD3B8E9C79ADFF35385287E6E2F4DAC8D096882474EB15B8DF679C7998CDB1F86624E772D5F84625330776BF493123E8EFCF83AC5B235204A9540474ABBF9D659FFE64F96B7E5A2573E0BCBA1EBAD4E5E1FBBFFCB74F1D4979BCDF7DE0BE100BE9485F305D4B7E1FF841AD22100783DEE710000000000000000000000000000000000000070C10F29ED03DE",
                "30750000789CEDD8DB8EC220100050E6FF7F7A13774B9942151BD6183DE7C10B9619048A608987CAE34BEAA5C96FD5DB8B43BC92DF465B678BD494467A1ADBA39CB4E6AA98C93E6E4FE4F6D492FED2D4CFB9BC349DB6B522BA3E7DB66D4F556A462D8DD6A21EFECB712F3953E6FA2A4A1EBA8BDD7B52EDE583354ED8979EDC7DAB122E353BE56374ED1375878927A7D0543C1AB71EB29ABDB76DC7727B7DF6F99BA9BB8B6D97B072035493D4A738142DF78A15767F78C71185A3FF9AA8FD5AF10137C4E14C15A59E5ED675633E8045BFB35E9A62E2247EAC1035483D74DF4FD205B86A519814B23F4F379F6DD3B86BC73EBFF7937CD4C772D667C301E882479E62C33AF95476DAC5B33DB6A798A9516F846653B0E55BFD4F091FC7FCF83ACD7AB420CA6102CD940EE7DB60FDDB3EA96FDB45AFFD2F2C873E6E75F2FA38FC2EE5E2A92FA78DF4F375211CC097B270BE818547062309C0A7F21B070000000000000000000000000000000000000017FC00C3B703DE"
        };
        String questNotify = "00000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String specificSection = "A82F0000789CEDD3370A02611445E179D889B1356741B1300710145130A0829B70FF2B1057207AC0E1F1DFAF98EE1433EF4E14C5C60ACFF7F3C7B848E21289CB244E90B802E2F85895BC738DC475123748DC74792AA72C494ED522719BC41D8DC4094B913BA7499C217196C43912E7B5EDAF58977CED1E89FB719DCA061AC9FFD8908C6444E23189271A89133625779E91784EE2058997245E69DB4ED85AA70A816DC8FFBC25F14E0B0B81EDC9480E243E6A6121B01319C999C4172D2C04762523B96924F281DDC9C21E5A98888888888888888878F202ADDA1D05";
        String generalSection = "AE100000789CEDD2490E82401484E157C6AB12BD9E5B075C0B720B82A238808243043DC49F34E948EDBF74BA5E99D28999C9DC471978593B82F704E77D15E66974206D1F092E083E117C26F842F095E0D2CB6DAB227FBE117C27B826B821F8E1E59DFB8B9EA4ED17C16F826701C09F6124EEA296DCB923F84BB04D0116C12382C7007B1ACD49610B829704AF080E095EFFDF4850B4216D4704C7046F094E869138CB0F2D2A32EC";
        String despair = "981C0000789CEDC13101000000C2A0F54F6D0A3FA000000000000000BE061C980001";

        byte[][] clearQuestBytes = new byte[clearQuests.length][];
        for (int i = 0; i < clearQuests.length; i++) {
            clearQuestBytes[i] = HexUtil.decodeHex(clearQuests[i]);
        }
        byte[] questNotifyBytes = HexUtil.decodeHex(questNotify);
        byte[] specificSectionBytes = HexUtil.decodeHex(specificSection);
        byte[] generalSectionBytes = HexUtil.decodeHex(generalSection);
        byte[] despairBytes = HexUtil.decodeHex(despair);

        for (Charac character : characters) {
            int characNo = character.getNo();
            Integer job = characInfoMapper.findJob(characNo);
            Assert.checkBetween(job, 0, 9, "无法识别角色的职业：" + characNo);

            byte[] clearQuest = clearQuestBytes[job];
            newCharacRequestMapper.clearAllQuests(characNo, clearQuest, questNotifyBytes);
            characTitleBookMapper.update(characNo, specificSectionBytes, generalSectionBytes, despairBytes);
            characStatMapper.unlockSlots(characNo);
        }
    }

    public void setMaxEquipLevel(List<Charac> characters) {
        for (Charac character : characters) {
            int characNo = character.getNo();
            int rows = characManageInfoMapper.setMaxEquipLevel(characNo);
            if (rows == 0) {
                characManageInfoMapper.insertMaxEquipLevel(characNo);
            }
        }
    }

    public void unlockSlots(List<Charac> characters) {
        for (Charac character : characters) {
            characStatMapper.unlockSlots(character.getNo());
        }
    }

    public void fillCloneAvatas(List<Charac> characters) {
        int[][] itemIds = new int[][]{
                {42601, 42603, 42605, 42609, 42611, 42615, 42607, 42613},
                {46601, 46603, 46605, 46609, 46611, 46615, 46607, 46613},
                {50601, 50603, 50605, 50609, 50611, 50615, 50607, 50613},
                {54601, 54603, 54605, 54609, 54611, 54615, 54607, 54613},
                {58601, 58603, 58605, 58609, 58611, 58615, 58607, 58613},
                {1690008, 1690009, 1690010, 1690012, 1690013, 1690015, 1690011, 1690014},
                {1810001, 1810003, 1810005, 1810009, 1810011, 1810015, 1810007, 1810013},
                {2050001, 2050003, 2050005, 2050009, 2050011, 2050015, 2050007, 2050013},
                {2170001, 2170003, 2170005, 2170009, 2170011, 2170015, 2170007, 2170013},
                {42601, 42603, 42605, 42609, 42611, 42615, 42607, 42613}
        };

        for (Charac character : characters) {
            fillAvatas(character.getNo(), itemIds);
        }
    }

    @SneakyThrows
    private void fillAvatas(int characNo, int[][] itemIds) {
        Integer job = characInfoMapper.findJob(characNo);
        Assert.checkBetween(job, 0, 9, "无法识别角色的职业：" + characNo);
        Integer equipAvataCount = userItemMapper.countEquipAvatas(characNo);
        if (equipAvataCount != 0) {
            throw new Exception("角色已经装备其他装扮：" + characNo);
        }

        int abilityNo = 0;
        String ipgAgencyNo = String.format("B11150101%010d", characNo);
        for (int slot = 0; slot < itemIds[job].length; ++slot) {
            int itemId = itemIds[job][slot];
            userItemMapper.addAvata(characNo, slot, itemId, abilityNo, ipgAgencyNo);
        }
    }

    public void addAvatas(List<Charac> characters, String commaSeperatedItemIds) {
        List<Integer> itemIds = ItemUtils.parseCommaSeperatedItemIds(commaSeperatedItemIds);
        for (Charac character : characters) {
            addAvatas(character.getNo(), itemIds);
        }
    }

    private void addAvatas(int characNo, List<Integer> itemIds) {
        String ipgAgencyNo = String.format("B11150101%010d", characNo);
        for (Integer itemId : itemIds) {
            Integer maxSlot = userItemMapper.findMaxAvatarSlot(characNo);
            int nextSlot = maxSlot == null ? 10 : maxSlot + 1;
            userItemMapper.addAvata(characNo, nextSlot, itemId, 0, ipgAgencyNo);
        }
    }

    public void addCreatures(List<Charac> characters, String commaSeperatedItemIds, boolean isEgg) {
        List<Integer> itemIds = ItemUtils.parseCommaSeperatedItemIds(commaSeperatedItemIds);
        int creatureType = isEgg ? 0 : 1;
        for (Charac character : characters) {
            addCreatures(character.getNo(), itemIds, creatureType);
        }
    }

    private void addCreatures(int characNo, List<Integer> itemIds, int creatureType) {
        inventoryMapper.setCreatureFlag(characNo, 1);
        for (Integer itemId : itemIds) {
            Integer maxSlot = creatureItemMapper.findMaxCreatureSlot(characNo);
            int nextSlot = maxSlot == null ? 0 : maxSlot + 1;
            creatureItemMapper.addCreature(characNo, nextSlot, itemId, creatureType);
        }
    }
}
