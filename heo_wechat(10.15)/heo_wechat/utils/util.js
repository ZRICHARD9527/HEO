const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}
function dateFormat(fmt, date) {
  let ret;
  const opt = {
      "Y+": date.getFullYear().toString(),        // 年
      "m+": (date.getMonth() + 1).toString(),     // 月
      "d+": date.getDate().toString(),            // 日
      "H+": date.getHours().toString(),           // 时
      "M+": date.getMinutes().toString(),         // 分
      "S+": date.getSeconds().toString()          // 秒
      // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
      ret = new RegExp("(" + k + ")").exec(fmt);
      if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
      };
  };
  return fmt;
}

function datestr_to_rest(d) {
  var leftTime = (new Date(d.replace(/-/g, '/')).getTime() - new Date()) / 1000 / 60;
  var days = parseInt(leftTime / 60 / 24, 10); //计算剩余的天数 
  var hours = parseInt(leftTime / 60 % 24, 10); //计算剩余的小时 
  var minutes = parseInt(leftTime % 60, 10); //计算剩余的分钟 
  return days + "天" + hours + "时" + minutes + "分后截止"
}

module.exports = {
  formatTime: formatTime,
  dateFormat:dateFormat,
  datestr_to_rest:datestr_to_rest
}
