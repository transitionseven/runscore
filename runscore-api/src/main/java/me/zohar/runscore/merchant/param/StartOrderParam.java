package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.domain.MerchantOrder;
import me.zohar.runscore.merchant.domain.MerchantOrderPayInfo;

@Data
public class StartOrderParam {

	/**
	 * 商户号
	 */
	@NotBlank
	private String merchantNum;

	/**
	 * 商户订单号
	 */
	@NotBlank
	private String orderNo;

	@NotBlank
	private String payType;

	/**
	 * 支付金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double amount;

	@NotBlank
	private String notifyUrl;

	@NotBlank
	private String returnUrl;

	private String attch;

	@NotBlank
	private String sign;

	public MerchantOrder convertToPo(String merchantId, Integer orderEffectiveDuration) {
		MerchantOrder po = new MerchantOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setGatheringAmount(this.getAmount());
		po.setGatheringChannelCode(this.getPayType());
		po.setSubmitTime(new Date());
		po.setOrderState(Constant.商户订单状态_等待接单);
		po.setMerchantId(merchantId);
		po.setUsefulTime(DateUtil.offset(po.getSubmitTime(), DateField.MINUTE, orderEffectiveDuration));
		return po;
	}

	public MerchantOrderPayInfo convertToPayInfoPo(String merchantOrderId) {
		MerchantOrderPayInfo po = new MerchantOrderPayInfo();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setMerchantOrderId(merchantOrderId);
		po.setNoticeState(Constant.商户订单支付通知状态_未通知);
		return po;
	}

}
