CREATE TABLE IF NOT EXISTS images(
    image_id UUID PRIMARY KEY DEFAULT uuid_generate_V4(),
    announcement_id UUID REFERENCES announcements(announcement_id) ON DELETE CASCADE,
    image_description VARCHAR(32),
    image_url TEXT NOT NULL
);
