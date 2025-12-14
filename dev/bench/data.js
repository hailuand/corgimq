window.BENCHMARK_DATA = {
  "lastUpdate": 1765693639406,
  "repoUrl": "https://github.com/hailuand/corgimq",
  "entries": {
    "JMH Benchmark": [
      {
        "commit": {
          "author": {
            "email": "6646502+hailuand@users.noreply.github.com",
            "name": "Andreas Hailu",
            "username": "hailuand"
          },
          "committer": {
            "email": "noreply@github.com",
            "name": "GitHub",
            "username": "web-flow"
          },
          "distinct": true,
          "id": "f8d6f5abc4e6ac0c5643be7df6df58f324fd344f",
          "message": "Merge pull request #169 from hailuand/bm-tune\n\nSlim down benchmarks",
          "timestamp": "2025-12-13T17:38:30-08:00",
          "tree_id": "e3c5fe95b5c9668fe67ddb2faa97820a44329a5c",
          "url": "https://github.com/hailuand/corgimq/commit/f8d6f5abc4e6ac0c5643be7df6df58f324fd344f"
        },
        "date": 1765693639094,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 10836.566324477628,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 10861.59374279118,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 207.58978579991384,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 214.2325799557155,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 328.8077698108726,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 327.3085786111877,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 2985.624689437294,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 3149.0419878093435,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 4648.9604038028965,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 4697.8932641128795,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 1099.8994047889935,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 1093.1616619931024,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 32.71479979783109,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 32.16351414442841,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 32.00396162745442,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 33.52699480504668,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 1.880953637932247,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 336.00074388473996,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 312.3571485848267,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 1058.0904376449018,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 1060.4202117264726,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 14238.459840799202,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 13917.934875866524,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 440.04927647385887,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 446.3418999648131,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 390.0272202364196,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 383.2710098792731,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 608.059134117784,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 616.2914661459217,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 2120.0423926360922,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 2123.07637462975,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 9056.813204376933,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 9245.01621850821,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 1407.7348938153668,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 1367.0059640671439,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 54.826958525234616,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 53.88716905128492,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 38.484477478903386,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 37.77866367515702,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 69.27380919400086,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 69.53121268004669,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 197.09802049062787,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 196.17772890869523,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 1340.6308818543046,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 1329.5608642876532,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 183039.45000692963,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 129.5681466540384,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 771.5453281167954,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 1174.4693734920497,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 752.0739094477314,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 5578.1526746135805,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 24237.398387287838,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 64.89226835890936,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 82.40708081776913,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 160.5535473877038,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 708.9699731247188,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 3520.4042327359966,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 294432.8175775182,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 528.230142100906,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 584.4461844586483,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 1138.6281409426986,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 764.3957111118391,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 3927.1766987393157,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 47620.08456650509,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 166.6751934301914,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 85.76066057559791,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 171.82353466441813,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 733.788839026644,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 3064.6724414367322,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}