local charset = {}  do
    for c = 65, 90  do table.insert(charset, string.char(c)) end
end

local function randomString(length)
    if not length or length <= 0 then return "" end
    return randomString(length - 1) .. charset[math.random(1, #charset)]
end

request = function()
  firstNamePrefix = randomString(math.random(1,5))
  lastNamePrefix = randomString(math.random(1,5))
  path = wrk.path .. "&firstNamePrefix=" .. firstNamePrefix .. "&lastNamePrefix=" .. lastNamePrefix
  return wrk.format("GET", path)
end

done = function(summary, latency, requests)
   file = io.open("result.csv", "a")
   io.output(file)
   latency90p = latency:percentile(90.) / 1000.
   throughput = summary.requests / (summary.duration / 1000000.)
   io.write(latency90p .. "," .. throughput .. "\n")
   io.close(file)
end
