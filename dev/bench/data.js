window.BENCHMARK_DATA = {
  "lastUpdate": 1765870594181,
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
      },
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
          "id": "c1cf3c2d3294ad45f221da94fbddd7a78218b09d",
          "message": "Add benchmark info to docs (#170)",
          "timestamp": "2025-12-14T10:01:04-08:00",
          "tree_id": "48e4958acde2ecfb5512fc547fb9165e5f0c12c0",
          "url": "https://github.com/hailuand/corgimq/commit/c1cf3c2d3294ad45f221da94fbddd7a78218b09d"
        },
        "date": 1765740563728,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 13316.241144851107,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 13611.582514824127,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 190.54458368512786,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 196.3817805742007,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 284.47248446220425,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 284.70976205099686,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 23.02556051853013,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 2892.847639144594,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 2738.787246712561,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 3491.5992388610803,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 3584.6606238475506,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 1412.6779130649654,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 1373.4903231036783,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 27.52906201529604,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 27.924498190193237,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 29.00701107926539,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 30.08024090712511,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 1.9632198821317388,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 324.4558639242907,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 342.23821642874077,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 851.9803425182108,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 911.7565875346041,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 14616.706463089484,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 14927.229052333083,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 392.47186178104914,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 383.3709244623977,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 308.1955040556266,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 317.8807982507002,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 518.9876142102713,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 519.9592623517794,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 2136.44378000653,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 2243.907935222217,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 6900.73719201817,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 6879.888532991179,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 1547.667232257387,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 1513.903336908984,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 45.566693439274154,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 46.30592895804047,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 32.81058503860444,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 33.408011773098615,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 63.403727969235845,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 61.15466340259313,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 225.8493406832691,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 226.632266380814,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 1164.6674534315432,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 1160.6521317085383,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 200746.1327242185,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 130.6234565033044,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 722.3392956573329,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 1209.2218508914416,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 687.020582277258,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 4253.8263895430355,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 25827.954727755918,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 63.63614363093387,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 95.07822186332794,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 166.1484924308086,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 659.0506007986879,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 2805.1600077593835,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 322367.94940438453,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 528.8498127074943,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 611.7892190877274,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 1126.6964785881983,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 659.1322135678181,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 2840.5950844329627,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 54982.54779603109,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 149.57795638314505,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 99.23566043142134,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 189.94723679788723,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 627.6294728407158,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 2304.087149315375,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
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
          "id": "0ff30e0ce0bc283673466e5002e0f069d8352939",
          "message": "Add message_time to composite index & provide ROWLOCK hint to MS SQL Server (#173)\n\n* Add message_time to table composite index\n\n* Provide ROWLOCK hint in MS SQL Server query\n\n* Test\n\n* Fiddle with alert threshold",
          "timestamp": "2025-12-15T22:07:18-08:00",
          "tree_id": "a23a11552084e095df744037d7e5e6577644e31f",
          "url": "https://github.com/hailuand/corgimq/commit/0ff30e0ce0bc283673466e5002e0f069d8352939"
        },
        "date": 1765870593000,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 12569.958972259952,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 12723.464154153831,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 195.28777342941058,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 206.6332365508294,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 295.7742787038181,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 302.37763117569386,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 23.627311088745216,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 22.98813336624023,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 1264.103731366041,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 1062.8950741233316,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 3565.4167115185946,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 6015.474984222628,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 1310.1958652607827,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 1350.1425431257007,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 29.52452328033678,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 29.830827898394215,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 31.049046759419422,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 31.47728146873675,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 7.183115332970786,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 7.590200546178297,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 28.758032687766217,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 28.299438288364605,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 885.6034368664717,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePopConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 945.7771461973232,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 15259.969441640666,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 15246.135073699072,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 417.660858667812,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 420.6253044493189,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 340.5083086757707,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 326.87705248407656,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 539.9651916217542,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 541.7249061274113,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 1951.0115784191264,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 1951.4526093068785,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 7092.047608650627,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 7263.7473701296885,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 1524.163557938784,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 1467.005155956776,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 50.20426362466581,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 50.13121296816984,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 34.51367286742578,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 34.45967182049621,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 71.71117403010555,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 69.24902436895358,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 207.0881458924261,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 205.5042061602826,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 1195.13480502281,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.concurrency.MessageQueuePushConcurrencyBenchmark.benchmarkPushConcurrency ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 1192.2377963791453,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 197381.35724922924,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 131.8800298362446,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 758.8096964670701,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 1278.563976934015,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 639.1134180966558,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 4315.865706555377,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 24206.556706401134,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 63.62907560959887,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 99.92718668645799,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 172.93979431042436,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 198.08139973548955,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePopThroughputBenchmark.benchmarkPop ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 2891.331959008119,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 329723.5557614621,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 545.561275260375,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 643.7451955477503,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 1166.8422421967623,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 704.8528773131353,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 2866.2659678694276,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 53582.73095970743,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 160.0613398352388,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 104.87007262536247,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 202.47517889629594,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 732.8122440801022,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.throughput.MessageQueuePushThroughputBenchmark.benchmarkPush ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 2326.3164186811837,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}