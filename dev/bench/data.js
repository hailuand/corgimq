window.BENCHMARK_DATA = {
  "lastUpdate": 1765993916053,
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
          "id": "e46a6338e8f18607d3a322021635bc2d5100e21d",
          "message": "Refactor benchmarks (#174)",
          "timestamp": "2025-12-17T08:35:49-08:00",
          "tree_id": "66603e4effb6aa8abc47b3ebf9de236a71ad48ca",
          "url": "https://github.com/hailuand/corgimq/commit/e46a6338e8f18607d3a322021635bc2d5100e21d"
        },
        "date": 1765993915589,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 0.22364745270995492,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 0.2233185267764654,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 11.329262292566003,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 9.440318522632495,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 14.652847980572554,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 14.620638632211316,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 6.791108165569679,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 7.279190541611572,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 2.134120534450399,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 1.9239294126095583,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 0.5221456603739464,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 0.5173282954491796,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"4\"} )",
            "value": 2.0904279235626135,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"H2\",\"threads\":\"12\"} )",
            "value": 2.2167155012734,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"4\"} )",
            "value": 74.84954381912178,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"12\"} )",
            "value": 74.39113171011469,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"4\"} )",
            "value": 142.20760362962122,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"MYSQL\",\"threads\":\"12\"} )",
            "value": 140.13187674137447,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"4\"} )",
            "value": 67.06402482593447,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"MSSQL\",\"threads\":\"12\"} )",
            "value": 65.6363606213973,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"4\"} )",
            "value": 19.956485904567085,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"12\"} )",
            "value": 19.53676734016809,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"4\"} )",
            "value": 3.0744810470358224,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushConcurrency.push ( {\"batchSize\":\"100\",\"dataSource\":\"POSTGRES\",\"threads\":\"12\"} )",
            "value": 3.0979451271323595,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"1\",\"dataSource\":\"H2\"} )",
            "value": 0.0031003787992233004,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 2.0482760849481356,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\"} )",
            "value": 1.8330888904664189,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"1\",\"dataSource\":\"MSSQL\"} )",
            "value": 0.9880633156850933,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 1.4590213472940143,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\"} )",
            "value": 0.32601941785921895,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"10\",\"dataSource\":\"H2\"} )",
            "value": 0.019085711157176572,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\"} )",
            "value": 6.112912881774264,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\"} )",
            "value": 12.544777041826293,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"10\",\"dataSource\":\"MSSQL\"} )",
            "value": 6.2573058777025246,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\"} )",
            "value": 1.868885644165956,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueuePushThroughput.push ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\"} )",
            "value": 0.4138976080861573,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"1\",\"dataSource\":\"H2\",\"threads\":\"6\"} )",
            "value": 19.396044826042317,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"1\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"6\"} )",
            "value": 10.151176233384303,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"1\",\"dataSource\":\"MYSQL\",\"threads\":\"6\"} )",
            "value": 4.194848901161258,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"1\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"6\"} )",
            "value": 4.03800288375578,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"1\",\"dataSource\":\"POSTGRES\",\"threads\":\"6\"} )",
            "value": 5.294564984615311,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"10\",\"dataSource\":\"H2\",\"threads\":\"6\"} )",
            "value": 0.12473366247591935,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"10\",\"dataSource\":\"COCKROACHDB\",\"threads\":\"6\"} )",
            "value": 36.870937580902556,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"10\",\"dataSource\":\"MYSQL\",\"threads\":\"6\"} )",
            "value": 12.063340752711845,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"10\",\"dataSource\":\"ORACLE_FREE\",\"threads\":\"6\"} )",
            "value": 6.604158899929561,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.github.hailuand.corgi.mq.benchmark.QueueUsage.listen ( {\"batchSize\":\"10\",\"dataSource\":\"POSTGRES\",\"threads\":\"6\"} )",
            "value": 1.9450445415466195,
            "unit": "ms/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          }
        ]
      }
    ]
  }
}