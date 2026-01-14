package com.gmlimsqi.business.ranch.dto;

import lombok.Data;

@Data
public class GetJCKCTestDTO {


    private String ccorpcode;

    private String cinvname;
    /**
     * 开始时间
     */
    private String starttime;

    /**
     * 结束时间
     */
    private String endtime;

    private String type;

}
