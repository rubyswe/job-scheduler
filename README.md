# Job Scheduler: Image Sorting & Keywording for Adobe Stock

A Spring Boot job scheduler that processes folders of photos in the background. It sorts out the images that meet Adobe Stock's resolution requirements. A planned ML stage will tag each image and generate keywords based on its subject.

I built it for my own photography workflow. Checking resolution and keywording every photo by hand before uploading to Adobe Stock is slow and repetitive, and this tool automates both.

---

## Tech Stack

### ✅ Currently Using

| Area | Tool | What It's For |
|---|---|---|
| Language | **Java 21** | Core language (records, switch expressions, virtual threads available) |
| Framework | **Spring Boot 4 (Web MVC)** | REST API, dependency injection, app lifecycle |
| Build | **Maven** (wrapper included) | Dependencies and builds, with `./mvnw` so no local Maven install is needed |
| Boilerplate | **Lombok** | Generates getters, setters and constructors (`@Data`, `@AllArgsConstructor`) |
| Concurrency | **`java.util.concurrent`** | `ArrayBlockingQueue` and `ConcurrentHashMap` for thread-safe job handling |
| File I/O | **`java.nio.file`** | Walking folders and moving files (`Files.walk`, `Files.move`) |
| Image I/O | **`javax.imageio`** | Reading image dimensions |
| Testing | **JUnit 5** + Spring Boot Test | Test framework (currently just a context-load test) |
| IDE | **IntelliJ IDEA** | Development |
| Version Control | **Git** + **GitHub** | Source control and hosting |

### 🗺️ Planned (From the Roadmap)

| Area | Tool | What I'll Learn |
|---|---|---|
| Logging | **SLF4J** + Logback | Structured logging with log levels, replacing `System.out` |
| Validation | **Jakarta Bean Validation** (`@Valid`) | Validating input at the API boundary |
| Image Metadata | **metadata-extractor** / `ImageReader` | Reading image dimensions from headers without decoding the whole file |
| Concurrency | **`ExecutorService`** + **virtual threads** | Thread pools and Java 21's lightweight threads |
| Database | **PostgreSQL** + **Spring Data JPA** | Persistence, ORM, and `SELECT ... FOR UPDATE SKIP LOCKED` job claiming |
| ML Model | **CLIP** (OpenAI / `open_clip`) | Matching images against a list of keywords without training a model |
| ML Model | **BLIP** (Hugging Face) | Generating titles and captions from images |
| ML Runtime | **PyTorch** + **Hugging Face Transformers** | Loading and running pretrained models |
| ML Service | **FastAPI** (Python) *or* **ONNX Runtime** / **DJL** (Java) | Serving models and tying Java and Python together |
| Metadata Writing | **ExifTool** | Writing IPTC/XMP keywords and titles into image files |
| Unit Testing | **JUnit 5** + **Mockito** | Testing pieces in isolation with mocks |
| Integration Testing | **Testcontainers** | Running tests against a real Postgres in Docker |
| CI/CD | **GitHub Actions** | Building and testing automatically on every push and PR |
| Containers | **Docker** + **Docker Compose** | Starting the app, database and ML service with one command |
| Metrics | **Micrometer** + **Prometheus** + **Grafana** | Tracking queue depth, job time and failure rate |
| Load Testing | **k6** | Measuring throughput and finding bottlenecks |
| File Watching | **`java.nio.file.WatchService`** | Starting jobs automatically when new files arrive |

### 💡 Recommended Extras

| Area | Tool | Why It's Worth It |
|---|---|---|
| API Docs | **springdoc-openapi** (Swagger UI) | Interactive API docs generated from your code, so reviewers can try the API in a browser |
| API Client | **Bruno** or **Postman** | Saving and sharing API requests instead of retyping `curl` commands |
| Health & Ops | **Spring Boot Actuator** | `/actuator/health` and `/actuator/metrics` endpoints, used by Docker health checks and Prometheus |
| DB Migrations | **Flyway** | Versioning database schema changes the way real teams do |
| Resilience | **Resilience4j** | Ready-made retry, circuit breaker and rate limiting, which is especially useful around the ML service |
| Config | **`@ConfigurationProperties`** | Type-safe settings for thresholds, paths and worker count |
| Code Coverage | **JaCoCo** | Coverage reports, plus a badge for the README |
| Formatting & Linting | **Spotless** + **Checkstyle** | Consistent code style enforced in CI |
| Static Analysis | **SonarCloud** (free for public repos) | Finds bugs and code smells, and gives you a quality badge |
| Dependency Updates | **Dependabot** | Automatic PRs that keep dependencies up to date |
| Diagrams | **Mermaid** | Architecture diagrams that GitHub renders right in the README |
| Messaging (stretch) | **RabbitMQ** or **Kafka** | Replacing the in-memory queue with a real message broker, a big plus for distributed-systems interviews |
| Caching (stretch) | **Redis** | Caching ML results so re-tagging the same image is instant |
| Deployment (stretch) | **AWS** (EC2 / ECS + S3) or **Fly.io** | A live demo link on your résumé |
| Commits | **Conventional Commits** (`feat:`, `fix:`, `docs:`) | A clean, readable history that reviewers notice |

---

## How It Works

```
 Client ──POST /jobs──▶ JobEndpoints ──submit──▶ JobQueue (bounded, capacity 12)
                                                     │
                                                   take()
                                                     ▼
 Client ◀─GET /jobs/{id}── status ◀──────────── Worker thread
                                                     │
                                         switch on Job.Type
                                                     ▼
                                               ImageSorter
                                   (moves 4K JPEGs into <folder>/4k/)
```

1. A client submits a job with a type and a folder location.
2. The job is added to a bounded `ArrayBlockingQueue`. If the queue is full, the API returns **503**. This is backpressure: the app rejects new work instead of piling it up.
3. A background worker thread takes the job and marks it `RUNNING`. It then sends the job to the right handler based on its type.
4. The job ends up `COMPLETED` or `FAILED`, and the client can check it at any time.

### Project Structure

```
src/main/java/io/github/rubyswe/job_scheduler/
├── JobSchedulerApplication.java   # Spring Boot entry point
├── core/
│   ├── Job.java                   # Job model + Status/Type enums
│   ├── JobQueue.java              # Bounded queue + id → job lookup map
│   ├── Worker.java                # Takes jobs off the queue and runs them
│   └── main.java                  # Manual test harness (to be replaced by JUnit tests)
├── endpoints/
│   ├── JobEndpoints.java          # REST API
│   ├── JobRequest.java            # Request body
│   └── WorkerStarter.java         # Starts the worker thread on startup
└── handlers/
    └── ImageSorter.java           # Resolution check + file moving
```

---

## Getting Started

**Requirements:** Java 21+

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

### API

**Submit a job**

```bash
curl -X POST http://localhost:8080/jobs \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Sort beach shoot",
        "description": "Pull out 4K images",
        "type": "SORT_BY_RESOLUTION",
        "location": "/path/to/photos"
      }'
```

| Response | Meaning |
|---|---|
| `202 Accepted` + job id | The job was queued |
| `503 Service Unavailable` | The queue is full; try again later |

**Check a job's status**

```bash
curl http://localhost:8080/jobs/<job-id>
```

| Response | Meaning |
|---|---|
| `200 OK` + job JSON | Includes `status`: `PENDING`, `RUNNING`, `COMPLETED` or `FAILED` |
| `404 Not Found` | No job with that id |

---

## Progress

### ✅ Done (v0)

- [x] `Job` model with a UUID id, a creation timestamp, and `Status`/`Type` enums
- [x] `JobQueue`: a bounded blocking queue plus a concurrent map for looking up jobs by id
- [x] `Worker`: a background thread that runs jobs
  - [x] Handles errors per job: one failed job doesn't kill the worker
  - [x] Stops cleanly when interrupted
- [x] `ImageSorter`:
  - [x] Scans a folder for `.jpg`/`.jpeg` files
  - [x] Checks for 3840×2160 or larger, in either orientation
  - [x] Moves matches into a `4k/` subfolder
- [x] REST API: `POST /jobs` and `GET /jobs/{id}`
- [x] Backpressure: returns 503 when the queue is full
- [x] Worker starts automatically with the app (`CommandLineRunner`)

### 🔧 To Fix

**Correctness & concurrency**
- [ ] **Check Adobe Stock's real resolution rule.** As far as I know, it requires a minimum of **4 megapixels** rather than 4K. Make the threshold configurable in `application.properties`.
- [ ] `JobQueue`: the queue is `static` but the map isn't. Make them consistent.
- [ ] Make job status safe to read across threads. The worker thread writes it and the HTTP threads read it, so look into `volatile` or the Java Memory Model.
- [ ] Close the `Files.walk` stream (try-with-resources).
- [ ] Clean up finished jobs from the map so memory doesn't keep growing.

**Performance**
- [ ] Read image dimensions from the file header instead of decoding the whole image with `ImageIO.read`. Options are `ImageReader` or the `metadata-extractor` library. Benchmark the before and after.

**Security**
- [ ] Validate `location`. Right now a client can point a job at any path on the machine. Only allow paths under a configured root folder.
- [ ] Validate request bodies (`@Valid`, `@NotNull`).

**Code quality**
- [ ] Replace `System.out.println` with SLF4J logging.
- [ ] Turn the commented-out tests in `core/main.java` into JUnit tests, then delete `main.java`.
- [ ] Rename `JonSchedulerApplicationTests` to `JobSchedulerApplicationTests`.
- [ ] Remove `@RestController` and the unused imports from `JobSchedulerApplication`.
- [ ] Remove the hardcoded `/Users/ruby/...` paths.
- [ ] Write descriptive commit messages.

---

## Roadmap

### Phase 1: Solid Scheduler
- [ ] Replace the single thread with a pool of N workers (`ExecutorService`) and try Java 21 **virtual threads**
- [ ] Retry failed jobs with exponential backoff
- [ ] Job priorities (`PriorityBlockingQueue`)
- [ ] Cancel jobs (`DELETE /jobs/{id}`)
- [ ] Let in-progress jobs finish when the app shuts down
- [ ] `GET /jobs` with filtering by status
- [ ] Store jobs in **PostgreSQL** with Spring Data JPA so they survive restarts
- [ ] Let several workers share the job table safely with `SELECT ... FOR UPDATE SKIP LOCKED`

### Phase 2: ML Tagging & Keywording
- [ ] New job type: `TAG_IMAGES`
- [ ] Generate keywords with **CLIP**, ranking a keyword list by how well each word matches the image
- [ ] Generate titles with an image-captioning model (e.g. BLIP)
- [ ] Decide how to connect the model:
  - [ ] Option A: a Python **FastAPI** service that the Java app calls
  - [ ] Option B: run it inside Java with **ONNX Runtime** or **DJL**
- [ ] Write keywords and titles into **IPTC/XMP metadata** (ExifTool) so Adobe Stock picks them up at upload
- [ ] Rank keywords by confidence, up to Adobe Stock's limit of 49 keywords
- [ ] Evaluate on a hand-tagged set of my own photos and report precision and recall

### Phase 3: Production Polish
- [ ] Unit tests (JUnit 5, Mockito) and integration tests (Testcontainers)
- [ ] CI with GitHub Actions
- [ ] Docker Compose (app, Postgres, ML service)
- [ ] Metrics with Micrometer, Prometheus and Grafana (queue depth, job time, failure rate)
- [ ] Load testing with k6, with throughput numbers published below
- [ ] Watch a folder (`WatchService`) and start jobs automatically when new photos arrive
- [ ] Simple web dashboard for submitting jobs and viewing their progress

---

## Design Decisions

| Decision | Why |
|---|---|
| Bounded queue | Rejecting work when full (503) protects the app from overload instead of letting the queue grow without limit. |
| Separate error handling for "stop the worker" and "this job failed" | One bad folder marks a single job `FAILED` without stopping the worker. |
| Handlers selected by `Job.Type` | New job types (like `TAG_IMAGES`) can be added without changing the queue or the worker loop. |

---

## Benchmarks

_Coming soon: images processed per minute, header-only vs. full-decode timing, and keyword precision and recall._
