package p.minn.aop.log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import p.minn.common.aop.log.LogAop;
import p.minn.common.utils.LogUtil;
import p.minn.privilege.entity.OperatorLog;
import p.minn.privilege.entity.Account;
import p.minn.privilege.repository.LogDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 */
@Component("logTableAop")
public class LogTableAop extends LogAop {

	@Autowired
	private LogDao logDao;

	@Override
	public void invoke(Method method,Object[] args)  {

		List<Map<String, String>> operatorlogdetails = null;
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		Account u = (Account) req.getSession().getAttribute(Constant.LOGINUSER);

		String resid = LogUtil.getDaoResourceKey(method);

		for (Object obj : args) {

			if (LogUtil.checkLog(obj)) {

				OperatorLog operatorLog = new OperatorLog();
				operatorLog.setUserId(u.getId());
				operatorLog.setUserIp(Utils.getRemoteAdd(req));
				operatorLog.setResId(resid);
				operatorLog.setSignature(LogUtil.getResourceKey(method));

				operatorlogdetails = LogUtil.getBeanMap(obj);
				log2table(operatorLog, operatorlogdetails);
			}
		}

	}

	@Transactional
	private void log2table(OperatorLog operatorLog,
			List<Map<String, String>> operatorlogdetails) {

		logDao.insertOperatorLog(operatorLog);
		for (Map<String, String> operatorLogDetail : operatorlogdetails) {
			logDao.insertOperatorLogDetail(operatorLog.getId(),
					operatorLogDetail);
		}

	}

}
